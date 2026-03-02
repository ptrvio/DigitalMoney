package com.homeBanking.usersservice.repository;

import com.homeBanking.usersservice.entities.AccountRequest;
import com.homeBanking.usersservice.feignCustomExceptions.CustomErrorDecoder;
import com.homeBanking.usersservice.feignCustomExceptions.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "accounts-service",
        path = "/accounts",
        configuration = {FeignConfig.class, CustomErrorDecoder.class}
)
public interface FeignAccountRepository {

    @PostMapping("/create")
    void createAccount(@RequestBody AccountRequest accountRequest);
}
