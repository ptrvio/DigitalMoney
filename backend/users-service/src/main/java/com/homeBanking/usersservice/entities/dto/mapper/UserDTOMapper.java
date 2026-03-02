package com.homeBanking.usersservice.entities.dto.mapper;


import com.homeBanking.usersservice.entities.User;
import com.homeBanking.usersservice.entities.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(
                user.getKeycloakId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getCvu(),
                user.getAlias()
        );
    }
}
