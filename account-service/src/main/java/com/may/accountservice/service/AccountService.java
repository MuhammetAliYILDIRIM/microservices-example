package com.may.accountservice.service;


import com.may.accountservice.dto.request.AccountDto;
import com.may.accountservice.dto.response.AccountListDto;
import com.may.client.contract.AccountEventDto;

import java.util.List;

public interface AccountService {

    void createAccount(AccountDto accountDto);

    AccountListDto getAccountByUserName(String username);

    List<AccountListDto> getAllAccounts();

    AccountEventDto getAccountByUserID(String id);
}
