package com.may.accountservice.controller;

import com.may.accountservice.dto.request.AccountRequest;
import com.may.accountservice.dto.response.AccountListResponse;
import com.may.accountservice.service.AccountService;
import com.may.client.contract.AccountEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.may.accountservice.constants.RestApiUrls.*;

@RestController
@RequestMapping(ACCOUNT)
@RequiredArgsConstructor
@Validated
public class AccountController {


    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createNewAccount(@Valid @RequestBody AccountRequest accountRequest) {
        accountService.createAccount(accountRequest);
        return ResponseEntity.ok("Success");
    }

    @GetMapping
    public ResponseEntity<List<AccountListResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping(USERNAME)
    public ResponseEntity<AccountListResponse> getAccountByUsername(@PathVariable String username) {
        return ResponseEntity.ok(accountService.getAccountByUserName(username));
    }

    @GetMapping(EVENT_ID)
    public ResponseEntity<AccountEventDto> getAccountByUserId(@PathVariable("id") String id) {
        return ResponseEntity.ok(accountService.getAccountByUserID(id));

    }
}
