package com.Bank.Bank.controllers;

import com.Bank.Bank.Security.UserDetailsImpl;
import com.Bank.Bank.dtos.AccountDto;
import com.Bank.Bank.models.Account;
import com.Bank.Bank.models.Transfer;
import com.Bank.Bank.models.User;
import com.Bank.Bank.repositories.AccountRepository;
import com.Bank.Bank.repositories.TransferRepository;
import com.Bank.Bank.request.FindByNumberRequest;
import com.Bank.Bank.request.TransferRequest;
import com.Bank.Bank.response.MessageResponse;
import com.Bank.Bank.services.AccountSevice;
import com.Bank.Bank.services.TransferService;
import com.Bank.Bank.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransferRepository transferRepository;

    @Autowired
    TransferService transferService;

    @Autowired
    UserService userService;

    @Autowired
    AccountSevice accountSevice;

    @PostMapping
    public ResponseEntity<?> registerAccount(@Valid @RequestBody AccountDto accountDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if (accountRepository.existsByNumber(accountDto.getNumber())) {
            MessageResponse error = new MessageResponse("Já existe uma conta com o número informado!");
            return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);

        User user = userService.getUser(userDetails);

        account.setUser(user);
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(value = "/transfer")
    public ResponseEntity<?> transferAccount(@Valid @RequestBody TransferRequest transferRequest,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        MessageResponse error = new MessageResponse();

        Optional<Account> source_account_optional       = accountRepository.findByNumber(transferRequest.getSource_account_number());
        Optional<Account> destination_account_optional  = accountRepository.findByNumber(transferRequest.getDestination_account_number());

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

        if(accountSevice.not_have_enough_balance(transferRequest, source_account)){
            error.setError("Saldo insuficiente!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        User user          = userService.getUser(userDetails);
        Transfer transfer  = transferService.transfer(transferRequest, source_account, destination_account, user);

        accountRepository.save(source_account);
        accountRepository.save(destination_account);
        transferRepository.save(transfer);

        return new ResponseEntity<>(transfer, HttpStatus.CREATED);
    }

    @PostMapping(value = "/balance")
    public ResponseEntity<?> balanceAccount(@Valid @RequestBody FindByNumberRequest numberRequest) {
        Optional<Account> account = accountRepository.findByNumber(numberRequest.getAccount_number());
        MessageResponse error     = new MessageResponse();

        if(!account.isPresent()){
            error.setError("Conta não encontrada para o usuário informado!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        numberRequest.setBalance(account.get().getBalance());

        return new ResponseEntity<>(numberRequest, HttpStatus.FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = new HashMap<>();

        List<String> errorsList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorsList.add(error.getDefaultMessage());
        });

        errors.put("error", errorsList);
        return errors;
    }

}
