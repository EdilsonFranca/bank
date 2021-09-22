package com.Bank.Bank.services;

import com.Bank.Bank.models.Account;
import com.Bank.Bank.models.Transfer;
import com.Bank.Bank.models.User;
import com.Bank.Bank.request.TransferRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public Transfer transfer(TransferRequest transferRequest, Account source_account, Account destination_account, User user) {
        transaction(transferRequest, source_account, destination_account);

        Transfer transfer = new Transfer();
        BeanUtils.copyProperties(transferRequest, transfer);

        transfer.setUser_transfer(user);
        return transfer;
    }

    public void transaction(TransferRequest transferRequest, Account source_account, Account destination_account) {
        source_account.setBalance(source_account.getBalance().subtract(transferRequest.getAmount()));
        destination_account.setBalance(destination_account.getBalance().add(transferRequest.getAmount()));
    }
}
