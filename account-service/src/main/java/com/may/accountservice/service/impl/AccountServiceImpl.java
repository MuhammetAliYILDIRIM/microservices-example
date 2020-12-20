package com.may.accountservice.service.impl;


import com.may.accountservice.dto.request.AccountRequest;
import com.may.accountservice.dto.response.AccountListResponse;
import com.may.accountservice.exception.AccountServiceException;
import com.may.accountservice.exception.ErrorType;
import com.may.accountservice.repository.AccountRepository;
import com.may.accountservice.repository.entity.Account;
import com.may.accountservice.repository.entity.Address;
import com.may.accountservice.service.AccountService;
import com.may.accountservice.util.HashingUtil;
import com.may.client.contract.AccountEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.may.accountservice.exception.ErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void createAccount(AccountRequest accountRequest) {

        if (accountRepository.findUserByEmail(accountRequest.getEmail()).isPresent())
            throw new AccountServiceException(ErrorType.EMAIL_IN_USE);

        if (accountRepository.findUserByUsername(accountRequest.getUsername()).isPresent())
            throw new AccountServiceException(ErrorType.USERNAME_IN_USE);

        Account newAccount = new Account();
        newAccount.setEmail(accountRequest.getEmail());
        newAccount.setFistName(accountRequest.getFirstName());
        newAccount.setLastName(accountRequest.getLastName());
        newAccount.setUsername(accountRequest.getUsername());
        newAccount.setPassword(HashingUtil.encode(accountRequest.getPassword()));
        newAccount.setPhoneNumber(accountRequest.getPhoneNumber());
        Address address = new Address();
        address.setAddressLineOne(accountRequest.getAddressLineOne());
        address.setAddressLineTwo(accountRequest.getAddressLineTwo());
        address.setCountry(accountRequest.getCountry());
        address.setCity(accountRequest.getCity());
        address.setTown(accountRequest.getTown());
        address.setPostCode(accountRequest.getPostCode());
        newAccount.setAddress(address);
        accountRepository.save(newAccount);
    }

    @Override
    public AccountListResponse getAccountByUserName(String username) {

        Account account = accountRepository.findUserByUsername(username)
                .orElseThrow(() -> new AccountServiceException(USER_NOT_FOUND));

        return AccountListResponse.builder()
                .email(account.getEmail())
                .fullName(account.getFistName() + " " + account.getLastName())
                .username(account.getUsername())
                .build();

    }

    @Override
    public List<AccountListResponse> getAllAccounts() {

        return accountRepository.findAll().stream()
                .map(account -> AccountListResponse.builder()
                        .email(account.getEmail())
                        .fullName(account.getFistName() + " " + account.getLastName())
                        .username(account.getUsername())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public AccountEventDto getAccountByUserID(String id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountServiceException(USER_NOT_FOUND));

        return AccountEventDto.builder().id(account.getId()).fullName(account.getFistName() + " " + account.getLastName()).build();
    }

}