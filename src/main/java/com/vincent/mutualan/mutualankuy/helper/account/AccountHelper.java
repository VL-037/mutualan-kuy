package com.vincent.mutualan.mutualankuy.helper.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.repository.AccountRepository;

@Component
public class AccountHelper {

  @Autowired
  public AccountRepository accountRepository;

  public Account findOneAccount(Long id) {

    return accountRepository.findById(id)
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(String.format("account with id %d does not exist", id)));
  }
}
