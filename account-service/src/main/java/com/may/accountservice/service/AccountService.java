package com.may.accountservice.service;


import com.may.accountservice.dto.request.AccountRequest;
import com.may.accountservice.dto.response.AccountListResponse;
import com.may.client.contract.AccountEventDto;

import java.util.List;

public interface AccountService {

    void createAccount(AccountRequest accountRequest);

    AccountListResponse getAccountByUserName(String username);

    List<AccountListResponse> getAllAccounts();

    AccountEventDto getAccountByUserID(String id);
}
