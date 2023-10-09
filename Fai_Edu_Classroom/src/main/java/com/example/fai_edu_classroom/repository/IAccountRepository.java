package com.example.fai_edu_classroom.repository;

import com.example.fai_edu_classroom.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT a FROM Account a WHERE " +
            "a.username LIKE CONCAT('%',:query, '%')")
    List<Account> searchAccounts(String query);
}
