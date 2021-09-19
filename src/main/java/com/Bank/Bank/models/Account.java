package com.Bank.Bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(	name = "account", uniqueConstraints = { @UniqueConstraint(columnNames = "number"),})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank(message="o Numero não pode ser vazios")
    @Size(max = 6 , message="tamanho maximo 6 caracteres")
    @Size(min = 6 , message="tamanho minimo 6 caracteres")
    @Column(unique = true)
    private String number;

    @NotNull(message="o Saldo não pode ser vazios")
    private BigDecimal balance;

    @ManyToOne
    private User user;

}
