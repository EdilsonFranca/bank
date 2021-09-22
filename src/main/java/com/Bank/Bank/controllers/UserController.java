package com.Bank.Bank.controllers;

import com.Bank.Bank.dtos.UserDto;
import com.Bank.Bank.models.User;
import com.Bank.Bank.repositories.UserRepository;
import com.Bank.Bank.response.MessageResponse;
import com.Bank.Bank.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/users")
public class UserController {

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  UserService userService;

  @PostMapping
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {

    if (userRepository.existsByEmail(userDto.getEmail())) {
         MessageResponse error = new MessageResponse("Já existe um usuário com o email informado.!");
      return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    userDto.setPassword(encoder.encode(userDto.getPassword()));
    User user = new User();
    BeanUtils.copyProperties(userDto, user);

    userService.save(user);

    return new ResponseEntity<>(user, HttpStatus.CREATED);
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
