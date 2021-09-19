package com.Bank.Bank.controllers;

import com.Bank.Bank.Security.UserDetailsImpl;
import com.Bank.Bank.dtos.AccountDto;
import com.Bank.Bank.models.Account;
import com.Bank.Bank.models.User;
import com.Bank.Bank.repositories.AccountRepository;
import com.Bank.Bank.response.MessageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @PostMapping
    public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountDto accountDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (accountRepository.existsByNumber(accountDto.getNumber())) {
            MessageResponse error = new MessageResponse("Já existe uma conta com o número informado!");
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);

        User user = new User();
        BeanUtils.copyProperties(userDetails, user);

        account.setUser(user);
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
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
