package com.Bank.Bank.repositories;

import com.Bank.Bank.models.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    boolean existsByNumber(String number);

    Optional<Account> getByNumber(String number);
}