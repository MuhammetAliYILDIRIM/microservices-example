package com.may.accountservice.repository;

import com.may.accountservice.repository.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findUserByUsername(String username);

    Optional<Account> findUserByEmail(String email);
}
