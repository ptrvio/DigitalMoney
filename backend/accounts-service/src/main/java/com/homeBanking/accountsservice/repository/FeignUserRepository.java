package com.homeBanking.accountsservice.repository;

import com.homeBanking.accountsservice.entities.UserDTO;
import com.homeBanking.accountsservice.feignCustomExceptions.CustomErrorDecoder;
import com.homeBanking.accountsservice.feignCustomExceptions.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "users-service",
        path = "/users",
        configuration = {FeignConfig.class, CustomErrorDecoder.class}
)
public interface FeignUserRepository {

    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable String id);

    @GetMapping("/all")
    List<UserDTO> getAllUser();

    @GetMapping("/keycloak-id/{kcId}")
    Long getUserByKeycloakId(@PathVariable String kcId);

    @GetMapping("/alias/{alias}")
    Long getUserIdByAlias(@PathVariable String alias);

    @GetMapping("/cvu/{cvu}")
    Long getUserIdByCvu(@PathVariable String cvu);

    @GetMapping("/cvu-user/{userId}")
    String getCvuByUserId(@PathVariable Long userId);

    @GetMapping("/kc/{id}")
    String getKcByUserId(@PathVariable Long id);
}
