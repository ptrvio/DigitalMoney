package com.homeBanking.usersservice.entities.dto.mapper;



import com.homeBanking.usersservice.entities.User;
import com.homeBanking.usersservice.entities.dto.UserKeycloak;

import java.util.function.Function;

public class UserKeycloakMapper implements Function<User, UserKeycloak> {
    @Override
    public UserKeycloak apply(User user) {
        return new UserKeycloak(
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getEmail()
        );
    }
}
