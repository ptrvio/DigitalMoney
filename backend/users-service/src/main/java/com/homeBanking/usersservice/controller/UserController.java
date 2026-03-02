package com.homeBanking.usersservice.controller;

import com.homeBanking.usersservice.entities.AccessKeycloak;
import com.homeBanking.usersservice.entities.Login;
import com.homeBanking.usersservice.entities.dto.*;
import com.homeBanking.usersservice.exceptions.BadRequestException;
import com.homeBanking.usersservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllUser() throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userService.getUserIdByKcId(id)));
    }

    @GetMapping("/kc/{id}")
    public ResponseEntity<?> getKcIdrByUserId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getKcIdIdByUserId(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO) throws Exception {

        var user = userService.createUser(userRegistrationDTO);
        Login loginData = new Login();
        loginData.setEmail(userRegistrationDTO.email());
        loginData.setPassword(userRegistrationDTO.password());
        AccessKeycloak accessKeycloak = userService.login(loginData);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new RegisterResponse(accessKeycloak, user));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessKeycloak> login(@RequestBody Login loginData) {
        return ResponseEntity.ok(userService.login(loginData));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userId.isEmpty()) {
            ResponseEntity.notFound().build();
        }

        userService.logout(userId);

        return ResponseEntity.ok("Succesfully logged out");
    }

    @PutMapping("/{username}/forgot-password")
    public void forgotPassword(@PathVariable String username) {
        userService.forgotPassword(username);
    }

    @PatchMapping("/update-alias")
    public ResponseEntity<?> updateAlias(@RequestBody NewAliasRequest newAlias) throws BadRequestException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateAlias(kcId,newAlias);
        Map<String, Object> body = new HashMap<>();
        body.put("status", 0);
        body.put("message", "Alias updated successfully");
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest userUpdateRequest) throws Exception {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO userUpdated = userService.updateUser(kcId, userUpdateRequest);

        if(userUpdated == null) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
    }

    @PatchMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody NewPasswordRequest passwordRequest) throws BadRequestException {
        String kcId = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updatePassword(kcId, passwordRequest);

        return ResponseEntity.ok("Password updated succesfully");
    }

    @GetMapping("/keycloak-id/{kcId}")
    public ResponseEntity<?> getUserByKeycloakId(@PathVariable String kcId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserIdByKcId(kcId));
    }

    @GetMapping("/alias/{alias}")
    public ResponseEntity<?> getUserIdByAlias(@PathVariable String alias) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserIdByAlias(alias));
    }

    @GetMapping("/cvu/{cvu}")
    public ResponseEntity<?> getUserIdByCvu(@PathVariable String cvu) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserIdByCvu(cvu));
    }

    @GetMapping("/cvu-user/{userId}")
    public ResponseEntity<?> getCvuIdByUsrId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCvuByUserId(userId));
    }
    @GetMapping("/name/{userId}")
    public ResponseEntity<?> getNameIdByUsrId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getNameByUserId(userId));
    }
}

