package com.homeBanking.usersservice.service;

import com.homeBanking.usersservice.entities.AccessKeycloak;
import com.homeBanking.usersservice.entities.AccountRequest;
import com.homeBanking.usersservice.entities.Login;
import com.homeBanking.usersservice.entities.User;
import com.homeBanking.usersservice.entities.dto.*;
import com.homeBanking.usersservice.entities.dto.mapper.UserDTOMapper;
import com.homeBanking.usersservice.entities.dto.mapper.UserUpdateDTOMapper;
import com.homeBanking.usersservice.exceptions.BadRequestException;
import com.homeBanking.usersservice.exceptions.ResourceNotFoundException;
import com.homeBanking.usersservice.repository.FeignAccountRepository;
import com.homeBanking.usersservice.repository.UserRepository;
import com.homeBanking.usersservice.utils.AliasCvuGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AliasCvuGenerator generator;

    @Autowired
    KeycloakService keycloakService;
    @Autowired
    private final UserDTOMapper userDTOMapper;

    @Autowired
    private final UserUpdateDTOMapper userUpdateDTOMapper;

    @Autowired
    private FeignAccountRepository feignAccountRepository;

    public UserService(UserRepository userRepository, AliasCvuGenerator generator, KeycloakService keycloakService, UserDTOMapper userDTOMapper, UserUpdateDTOMapper userUpdateDTOMapper, FeignAccountRepository feignAccountRepository) {
        this.userRepository = userRepository;
        this.generator = generator;
        this.keycloakService = keycloakService;
        this.userDTOMapper = userDTOMapper;
        this.userUpdateDTOMapper = userUpdateDTOMapper;
        this.feignAccountRepository = feignAccountRepository;
    }


    public UserDTO createUser (UserRegistrationDTO userInformation) throws Exception {

        checkUserRequest(userInformation);

        Optional<User> userEmailOptional = userRepository.findByEmail(userInformation.email());
        Optional<User> userUsernameOptional = userRepository.findByUserName(userInformation.email());
        List<User> users = userRepository.findAll();
        String newCvu= "";
        String newAlias= "";
        String finalNewCvu = newCvu;
        String finalNewAlias = newAlias;


        if(userEmailOptional.isPresent()) {
            throw new BadRequestException("Email already exists");

        }

        if(userUsernameOptional.isPresent()) {
            throw new BadRequestException("Username already exists");
        }


        //check if cvu exists in DB and creates a new one
        do {
            newCvu = generator.generateCvu();
        } while (users.stream().anyMatch(user -> user.getCvu().equals(finalNewCvu)));

        //check if alias exists in DB and creates a new one
        do {
           newAlias= generator.generateAlias();
        } while (users.stream().anyMatch(user -> user.getAlias().equals(finalNewAlias)));

        var newUser = User.builder()
                .firstName(userInformation.firstName())
                .lastName(userInformation.lastName())
                .dni(userInformation.dni())
                .userName(userInformation.email())
                .email(userInformation.email())
                .phone(userInformation.phone())
                .cvu(newCvu)
                .alias(newAlias)
                .build();


        //register user in KC:
        User userKc = keycloakService.createUser(newUser,userInformation);
        newUser.setKeycloakId(userKc.getKeycloakId());
        //register in database
        User userSaved = userRepository.save(newUser);

        //create account for user
        feignAccountRepository.createAccount(new AccountRequest(userSaved.getId()));

        return new UserDTO(String.valueOf(userSaved.getId()),userInformation.firstName(), userInformation.lastName(), userInformation.email(), userInformation.phone(), newCvu, newAlias);
    }

    private void checkUserRequest(UserRegistrationDTO userInformation) throws BadRequestException {
        String phoneNumberPattern = "\\b\\d+\\b";
        String emailPattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b";
        if(userInformation.firstName().isEmpty()     ||
           userInformation.lastName().isEmpty() ||
           !userInformation.email().matches(emailPattern) ||
           !userInformation.phone().matches(phoneNumberPattern) ||
           userInformation.password().isEmpty()) {
            throw new BadRequestException("Field wrong or missing");
        }
    }

    public UserDTO getUserById(Long id) {
       return userRepository.findById(id)
               .map(userDTOMapper)
               .orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    public AccessKeycloak login(Login loginData) {

        userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return keycloakService.login(loginData);
    }

    public Optional<User> findByEmail(String email) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new Exception("User not found!");
        }
        return user;
    }

    public List<UserDTO> findAllUsers() throws Exception {
        return userRepository.findAll().stream().map(userDTOMapper).toList();
    }

    public void logout(String userId) {
        keycloakService.logout(userId);
    }

    public void forgotPassword(String username) {
        keycloakService.forgotPassword(username);
    }

    public void updateAlias(String kcId, NewAliasRequest newAlias) throws BadRequestException {
        String aliasRequest = newAlias.getAlias();

        checkAliasField(aliasRequest);

        Optional<User> userOptional = userRepository.findByKeycloakId(kcId);

        if(userOptional.isEmpty()) {
            throw  new ResourceNotFoundException("User not found");
        }

        User userFound = userOptional.get();

        Optional<User> aliasOptional = userRepository.findByAlias(aliasRequest);

        if(aliasOptional.isPresent()) {
            throw new BadRequestException("Alias already being used");
        } else {
            userFound.setAlias(aliasRequest);
        }

        userRepository.save(userFound);
    }

    @Transactional
    public UserDTO updateUser(String id, UpdateUserRequest updateUserRequest) throws BadRequestException {
        Optional<User> userOptional = userRepository.findByKeycloakId(id);

        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }

        User userFound = userOptional.get();

        userFound.setFirstName(updateUserRequest.firstName());
        userFound.setLastName(updateUserRequest.lastName());
        userFound.setDni(updateUserRequest.dni());
        userFound.setEmail(updateUserRequest.email());
        userFound.setPhone(updateUserRequest.phone());

        UserUpdateDTO userUpdateDTO = userUpdateDTOMapper.toUserUpdateDTO(userFound);

        userRepository.save(userFound);
        keycloakService.updateUser(userOptional.get(), userUpdateDTO);

        return new UserDTO(String.valueOf(userFound.getId()), userFound.getFirstName(), userFound.getLastName(), userFound.getEmail(),
                userFound.getPhone(), userFound.getCvu(), userFound.getAlias());
    }

    private void checkAliasField (String alias) throws BadRequestException {

        String pattern = "\\b(?:[a-zA-Z]+\\.?)+\\b";

        //if (alias == null || alias.length() == 0) {
        if (alias == null || alias.isEmpty()) {
                throw new BadRequestException("No alias found");
        }

        if (!alias.matches(pattern)) {
            throw new BadRequestException("Alias can't contain numbers");
        }

        if(alias.trim().length()<=3) {
            throw  new BadRequestException("alias must have at least 4 characters");
        }

        if(userRepository.findByAlias(alias).isPresent()) {
            throw  new BadRequestException("alias already exists");
        }
    }

    @Transactional
    public void updatePassword(String kcId, NewPasswordRequest passwordRequest) throws BadRequestException {
        if(!passwordRequest.getPassword().equals(passwordRequest.getPasswordRepeated())) {
            throw new BadRequestException("Passwords must be equals");
        }
        Optional<User> userOptional = userRepository.findByKeycloakId(kcId);

        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("user not found");
        } else {
            User userFound = userOptional.get();
            UserUpdateDTO userUpdateDTO = userUpdateDTOMapper.toUserUpdateDTO(userFound);
            userUpdateDTO.setPassword(passwordRequest.getPassword());
            keycloakService.updateUser(userOptional.get(), userUpdateDTO);
        }
    }

    public Long getUserIdByKcId(String kcId) {
        Optional<User> userOptional = userRepository.findByKeycloakId(kcId);
        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return userOptional.get().getId();
        }
    }

    public String getKcIdIdByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return userOptional.get().getKeycloakId();
        }
    }

    public Long getUserIdByAlias(String alias) {
        Optional<User> userOptional = userRepository.findByAlias(alias);
        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return userOptional.get().getId();
        }
    }

    public Long getUserIdByCvu(String cvu) {
        Optional<User> userOptional = userRepository.findByCvu(cvu);
        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return userOptional.get().getId();
        }
    }

    public String getCvuByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return userOptional.get().getCvu();
        }
    }

    public String getNameByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        } else {
            return userOptional.get().getFirstName() + " " + userOptional.get().getLastName();
        }
    }
}
