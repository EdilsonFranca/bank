package com.Bank.Bank.repositories;

import com.Bank.Bank.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, String> {}
