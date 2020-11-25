package com.may.accountservice.controller;

import com.may.accountservice.dto.request.AccountDto;
import com.may.accountservice.dto.response.AccountListDto;
import com.may.accountservice.service.AccountService;
import com.may.client.contract.AccountEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.may.accountservice.constants.RestApiUrls.*;

@RestController
@RequestMapping(ACCOUNT)
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createNewAccount(@RequestBody AccountDto accountDto) {
        accountService.createAccount(accountDto);
        return ResponseEntity.ok("Success");
    }

    @GetMapping
    public ResponseEntity<List<AccountListDto>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping(USERNAME)
    public ResponseEntity<AccountListDto> getAccountByUsername(@PathVariable String username) {
        return ResponseEntity.ok(accountService.getAccountByUserName(username));
    }

    @GetMapping(EVENT_ID)
    public ResponseEntity<AccountEventDto> getAccountByUserId(@PathVariable("id") String id) {
        return ResponseEntity.ok(accountService.getAccountByUserID(id));

    }
}
