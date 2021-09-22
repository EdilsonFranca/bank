package com.Bank.Bank.services;

import com.Bank.Bank.models.Account;
import com.Bank.Bank.request.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public class AccountSevice {

    public boolean not_have_enough_balance(TransferRequest transferRequest, Account source_account) {
        return source_account.getBalance().compareTo(transferRequest.getAmount()) != 1;
    }
}
