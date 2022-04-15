package com.vincent.mutualan.mutualankuy.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vincent.mutualan.mutualankuy.entity.Account;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    void findOneByUsername_shouldReturnNotNull() {
        String username = "vincent.low";

        Account expected = new Account();
        expected.setFirstName("Vincent");
        expected.setLastName("Low");
        expected.setBirthDate(LocalDate.MIN);
        expected.setUsername(username);
        accountRepository.save(expected);

        Account result = accountRepository.findOneByUsername(username)
                .stream()
                .findFirst()
                .orElse(null);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void findOneByUsername_shouldReturnNull() {
        String username = "vincent.low";

        Account result = accountRepository.findOneByUsername(username)
                .stream()
                .findFirst()
                .orElse(null);

        Assertions.assertEquals(null, result);
    }
}