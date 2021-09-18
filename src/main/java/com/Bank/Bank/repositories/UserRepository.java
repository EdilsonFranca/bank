package com.Bank.Bank.repositories;

import com.Bank.Bank.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String name);
}
