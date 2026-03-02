package com.homeBanking.usersservice.entities.dto;

public record UpdateUserRequest (String firstName,
                                 String lastName,
                                 String email,
                                 String dni,
                                 String phone) {
}
