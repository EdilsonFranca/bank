package com.Bank.Bank;

import com.Bank.Bank.models.Account;
import com.Bank.Bank.request.TransferRequest;
import com.Bank.Bank.services.AccountSevice;
import com.Bank.Bank.services.TransferService;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTransferAccount {

    private AccountSevice accountSevice     = new AccountSevice();
    private TransferService transferService = new TransferService();

    private TransferRequest transfer    = new TransferRequest();
    private Account account             = new Account();
    private Account destination_account = new Account();

    @Test
    public void test_have_enough_balance() {
        this.transfer.setAmount(new BigDecimal("25.48"));
        this.account.setBalance(new BigDecimal("100.00"));

        boolean haveBalance = accountSevice.not_have_enough_balance(this.transfer,this.account);

        assertEquals(false,haveBalance);
    }

    @Test
    public void test_not_have_enough_balance() {
        this.transfer.setAmount(new BigDecimal("125.48"));
        this.account.setBalance(new BigDecimal("100.00"));

        boolean haveBalance = accountSevice.not_have_enough_balance(this.transfer,this.account);

        assertEquals(true,haveBalance);

    }

    @Test
    public void test_transaction() {
        this.transfer.setAmount(new BigDecimal("10.00"));
        this.account.setBalance(new BigDecimal("100.00"));
        this.destination_account.setBalance(new BigDecimal("100.00"));

         transferService.transaction( transfer, account, destination_account);

        assertEquals(new BigDecimal("90.00") ,this.account.getBalance());
        assertEquals(new BigDecimal("110.00"),this.destination_account.getBalance());
    }
}
