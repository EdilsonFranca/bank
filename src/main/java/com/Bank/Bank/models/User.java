package com.Bank.Bank.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@Entity
@Table(	name = "users", 
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "name"),
			@UniqueConstraint(columnNames = "email") 
		})

public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@NotBlank(message="o Nome não pode ser vazios")
	@Size(max = 20 , message="tamanho maximo 20 caracteres")
	@Column(unique = true)
	private String name;

	@NotBlank(message="o Email não pode ser vazios")
	@Email(message="o Email invalido")
	@Size(max = 50)
	@Column(unique = true)
	private String email;

	@NotBlank(message="a Senha não pode ser vazios")
	@Size(min = 8  , message="Senha muito curta")
	@Size(max = 20)
	@JsonIgnore
	private String password;

}