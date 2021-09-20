package com.Bank.Bank.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
public class FindByNumberRequest {

    @NotBlank(message="Numero da conta  não pode ser vazios")
    private String account_number;

    private BigDecimal balance;
}
