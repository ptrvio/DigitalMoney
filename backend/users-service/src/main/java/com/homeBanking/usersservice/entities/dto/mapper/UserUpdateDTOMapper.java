package com.homeBanking.usersservice.entities.dto.mapper;

import com.homeBanking.usersservice.entities.User;
import com.homeBanking.usersservice.entities.dto.UserUpdateDTO;
import org.springframework.stereotype.Service;

@Service
public class UserUpdateDTOMapper{

    public UserUpdateDTO toUserUpdateDTO(User user){
    var userUpdateDTO = UserUpdateDTO.builder()
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .dni(user.getDni())
            .phone(user.getPhone())
            .build();
        return userUpdateDTO;
    }
}
