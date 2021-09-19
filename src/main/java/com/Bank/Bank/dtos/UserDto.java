package com.Bank.Bank.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserDto {
    @NotBlank(message="o Nome não pode ser vazios")
    @Size(max = 20 , message="tamanho maximo 20 caracteres")
    @Size(min = 5 ,  message="tamanho minimo 5 caracteres")
    private String name;

    @NotBlank(message="o Email não pode ser vazios")
    @Email(message="o Email invalido")
    private String email;

    @NotBlank(message="a Senha não pode ser vazios")
    @Size(min = 8  , message="Senha muito curta")
    private String password;
}
