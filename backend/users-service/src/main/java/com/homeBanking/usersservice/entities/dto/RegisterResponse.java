package com.homeBanking.usersservice.entities.dto;

import com.homeBanking.usersservice.entities.AccessKeycloak;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class RegisterResponse {
    private AccessKeycloak accessKeycloak;
    private UserDTO userDTO;
}
