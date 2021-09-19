package com.Bank.Bank.request;

import com.Bank.Bank.dtos.UserDto;
import com.Bank.Bank.models.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class TransferRequest {
    @NotBlank(message="número da conta de origem não pode ser vazios")
    private String source_account_number;

    @NotBlank(message="número da conta de destino não pode ser vazios")
    private String destination_account_number;

    @NotNull(message="o Saldo não pode ser vazios")
    @Min(value = 0, message = "saldo deve ser maior ou igual a zero")
    private BigDecimal amount;

    private User user_transfer;
}
