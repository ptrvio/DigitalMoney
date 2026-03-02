package com.homeBanking.usersservice.entities.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateDTO{
        private String firstName;
        private String lastName;
        private String email;
        private String dni;
        private String phone;
        @JsonIgnore
        private String password;
}
