package com.homeBanking.usersservice.entities.dto;

public record UserDTO(
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String cvu,
        String alias
) {
}
