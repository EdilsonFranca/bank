package com.Bank.Bank.controllers;

import com.Bank.Bank.Security.UserDetailsImpl;
import com.Bank.Bank.dtos.AccountDto;
import com.Bank.Bank.models.Account;
import com.Bank.Bank.models.Transfer;
import com.Bank.Bank.models.User;
import com.Bank.Bank.repositories.AccountRepository;
import com.Bank.Bank.repositories.TransferRepository;
import com.Bank.Bank.request.TransferRequest;
import com.Bank.Bank.response.MessageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransferRepository transferRepository;

    @PostMapping
    public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountDto accountDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (accountRepository.existsByNumber(accountDto.getNumber())) {
            MessageResponse error = new MessageResponse("Já existe uma conta com o número informado!");
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);

        User user = getUser(userDetails);

        account.setUser(user);
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = "/transfer")
    public ResponseEntity<?> transferAccount(@Valid @RequestBody TransferRequest transferRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageResponse error = new MessageResponse();

        Optional<Account> source_account_optional       = accountRepository.getByNumber(transferRequest.getSource_account_number());
        Optional<Account> destination_account_optional  = accountRepository.getByNumber(transferRequest.getDestination_account_number());

        boolean source_account_not_found      = !source_account_optional.isPresent();
        boolean destination_account_not_found = !destination_account_optional.isPresent();

        if(source_account_not_found) {
              error.setError("Conta de origem não encontrada para o usuário informado!");
             return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        if(destination_account_not_found) {
            error.setError("Conta de destino não encontrada para o usuário informado!");
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        Account source_account      = source_account_optional.get();
        Account destination_account = destination_account_optional.get();

        if(source_account.getBalance().compareTo(transferRequest.getAmount()) != 1){
            error.setError("Saldo insuficiente!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        source_account.setBalance(source_account.getBalance().subtract(transferRequest.getAmount()));
        destination_account.setBalance(destination_account.getBalance().add(transferRequest.getAmount()));

        accountRepository.save(source_account);
        accountRepository.save(destination_account);

        User user = getUser(userDetails);

        Transfer transfer = new Transfer();

        BeanUtils.copyProperties(transferRequest, transfer);

        transfer.setUser_transfer(user);

        transferRepository.save(transfer);
        return new ResponseEntity<>(transfer, HttpStatus.CREATED);
    }

    private User getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = new User();
        BeanUtils.copyProperties(userDetails, user);
        return user;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
