package com.Bank.Bank.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
public class AccountDto {
    private Long id;

    @NotBlank(message="o Numero não pode ser vazios")
    @Size(max = 6 , message="tamanho maximo 6 caracteres")
    @Size(min = 6 , message="tamanho minimo 6 caracteres")
    private String number;

    @NotNull(message="o Saldo não pode ser vazios")
    @Min(value = 0, message = "saldo deve ser maior ou igual a zero")
    private BigDecimal balance;

    private UserDto userDto;
}
