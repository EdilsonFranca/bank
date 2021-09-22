package com.Bank.Bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(	name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank(message="número da conta de origem não pode ser vazios")
    private String source_account_number;

    @NotBlank(message="número da conta de destino não pode ser vazios")
    private String destination_account_number;

    @NotNull(message="o valor não pode ser vazios")
    @Min(value = 1, message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    @OneToOne
    private User user_transfer;
}
