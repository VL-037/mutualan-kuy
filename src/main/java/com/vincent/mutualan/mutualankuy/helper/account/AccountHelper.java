package com.vincent.mutualan.mutualankuy.helper.account;

import com.vincent.mutualan.mutualankuy.entity.AccountRelationship;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.CreateAccountRelationshipRequest;
import com.vincent.mutualan.mutualankuy.repository.AccountRelationshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.repository.AccountRepository;

@Component
public class AccountHelper {

  @Autowired
  public AccountRepository accountRepository;

  @Autowired
  public AccountRelationshipRepository accountRelationshipRepository;

  public Account findOneAccount(Long id) {

    return accountRepository.findById(id)
        .stream()
        .findFirst()
        .orElse(null);
  }

  public Boolean isPresent(String username) {

    return accountRepository.findOneByUsername(username)
            .isPresent();
  }

  public AccountRelationship findOneRelationship(CreateAccountRelationshipRequest request) {

    return accountRelationshipRepository.findOneAccountRelationship(request.getFollowerId(), request.getFollowedId())
            .stream()
            .findFirst()
            .orElse(null);
  }
}
