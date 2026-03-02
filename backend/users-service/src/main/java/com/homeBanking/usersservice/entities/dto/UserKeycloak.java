package com.homeBanking.usersservice.entities.dto;

public record UserKeycloak(
        String name,
        String lastName,
        String username,
        String email
) {
}
