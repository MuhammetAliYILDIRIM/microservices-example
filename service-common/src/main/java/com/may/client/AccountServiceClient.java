package com.may.client;

import com.may.client.contract.AccountEventDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("account-service")
public interface AccountServiceClient {

    @RequestMapping("v1/account/event/{id}")
    ResponseEntity<AccountEventDto> getUserByUserId(@PathVariable("id") String id);

}
