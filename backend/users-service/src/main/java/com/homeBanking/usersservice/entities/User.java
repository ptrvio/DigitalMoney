package com.homeBanking.usersservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FirstName",nullable = false)
    private String firstName;

    @Column(name = "LastName",nullable = false)
    private String lastName;

    @Column(name= "UserName", nullable = false, unique = true)
    private String userName;

    @Column(name = "Email",nullable = false, unique = true)
    private String email;

    @Column(name = "Dni",nullable = false)
    private String dni;

    @Column(name = "Phone",nullable = false)
    private String phone;

    @Column(name = "CVU",nullable = false, unique = true)
    private String cvu;

    @Column(name = "Alias",nullable = false, unique = true)
    private String alias;

    @JsonIgnore
    private String keycloakId;

    public User(String firstName, String lastName, String userName, String email, String dni,String phone, String cvu, String alias/*, String password*/) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.cvu = cvu;
        this.alias = alias;
    }

    public static User toUser(UserRepresentation userRepresentation) {
        User user = new User();
        user.setKeycloakId(userRepresentation.getId());
        user.setUserName(userRepresentation.getUsername());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEmail(userRepresentation.getEmail());
        return user;
    }
}
