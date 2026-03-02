package com.homeBanking.transactionsservice.repository;

import com.homeBanking.transactionsservice.entities.UserDTO;
import com.homeBanking.transactionsservice.feignCustomExceptions.CustomErrorDecoder;
import com.homeBanking.transactionsservice.feignCustomExceptions.FeignConfig;
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

    @GetMapping("/name/{userId}")
    String getNameByUserId(@PathVariable Long userId);
}
