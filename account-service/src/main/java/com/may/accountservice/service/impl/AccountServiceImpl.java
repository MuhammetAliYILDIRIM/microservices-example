package com.may.accountservice.service.impl;


import com.may.accountservice.dto.request.AccountDto;
import com.may.accountservice.dto.response.AccountListDto;
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
    public void createAccount(AccountDto accountDto) {

        if (accountRepository.findUserByEmail(accountDto.getEmail()).isPresent())
            throw new AccountServiceException(ErrorType.EMAIL_IN_USE);

        if (accountRepository.findUserByUsername(accountDto.getUsername()).isPresent())
            throw new AccountServiceException(ErrorType.USERNAME_IN_USE);

        Account newAccount = new Account();
        newAccount.setEmail(accountDto.getEmail());
        newAccount.setFistName(accountDto.getFirstName());
        newAccount.setLastName(accountDto.getLastName());
        newAccount.setUsername(accountDto.getUsername());
        newAccount.setPassword(HashingUtil.encode(accountDto.getPassword()));
        newAccount.setPhoneNumber(accountDto.getPhoneNumber());
        Address address = new Address();
        address.setAddressLineOne(accountDto.getAddressLineOne());
        address.setAddressLineTwo(accountDto.getAddressLineTwo());
        address.setCountry(accountDto.getCountry());
        address.setCity(accountDto.getCity());
        address.setTown(accountDto.getTown());
        address.setPostCode(accountDto.getPostCode());
        newAccount.setAddress(address);
        accountRepository.save(newAccount);
    }

    @Override
    public AccountListDto getAccountByUserName(String username) {

        Account account = accountRepository.findUserByUsername(username)
                .orElseThrow(() -> new AccountServiceException(USER_NOT_FOUND));

        return AccountListDto.builder()
                .email(account.getEmail())
                .fullName(account.getFistName() + " " + account.getLastName())
                .username(account.getUsername())
                .build();

    }

    @Override
    public List<AccountListDto> getAllAccounts() {

        return accountRepository.findAll().stream()
                .map(account -> AccountListDto.builder()
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