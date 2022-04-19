package com.vincent.mutualan.mutualankuy.service.impl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_CONFLICT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NO_CONTENT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_UNPROCESSABLE;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.getBaseResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.entity.AccountRelationship;
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.account.AccountResponse;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.AccountRelationshipResponse;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.CreateAccountRelationshipRequest;
import com.vincent.mutualan.mutualankuy.repository.AccountRelationshipRepository;
import com.vincent.mutualan.mutualankuy.repository.AccountRepository;
import com.vincent.mutualan.mutualankuy.service.AccountService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AccountRelationshipRepository accountRelationshipRepository;

  @Autowired
  private AccountHelper accountHelper;

  @Override
  public BaseResponse<?> createOne(CreateAccountRequest request) {

    if (Objects.isNull(request))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    if (accountHelper.isPresent(request.getUsername()))
      return getBaseResponse(String.format("username is taken: %s", request.getUsername()),
          STATUS_CONFLICT());

    Account newAccount = saveOneAccount(request);

    return getBaseResponse(toAccountResponse(newAccount), STATUS_OK());
  }

  @Override
  public BaseResponse<?> createMany(List<CreateAccountRequest> requests) {

    if (Objects.isNull(requests))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    for (CreateAccountRequest request : requests) {
      if (accountHelper.isPresent(request.getUsername()))
        return getBaseResponse(String.format("username is taken: %s", request.getUsername()),
            STATUS_CONFLICT());
    }

    List<Account> newAccounts = requests.stream()
        .map(this::toAccount)
        .collect(Collectors.toList());

    if (newAccounts.size() == 0)
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    List<Account> savedAccounts = accountRepository.saveAll(newAccounts);

    List<AccountResponse> accountResponses = savedAccounts.stream()
        .map(this::toAccountResponse)
        .collect(Collectors.toList());

    return getBaseResponse(accountResponses, STATUS_OK());
  }

  @Override
  public BaseResponse<List<AccountResponse>> findAll() {

    List<Account> accounts = accountRepository.findAll();
    List<AccountResponse> accountResponses = accounts.stream()
        .map(this::toAccountResponse)
        .collect(Collectors.toList());

    return getBaseResponse(accountResponses, STATUS_OK());
  }

  @Override
  public BaseResponse<?> findById(Long id) {
    
    if(Objects.isNull(id))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    Account account = accountHelper.findOneAccount(id);
    if (Objects.isNull(account))
      return getBaseResponse(String.format("account with id %d does not exist", id), STATUS_NOT_FOUND());

    return getBaseResponse(toAccountResponse(account), STATUS_OK());
  }

  @Override
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public BaseResponse<?> updateOne(Long id, UpdateAccountRequest request) {

    Account account = accountHelper.findOneAccount(id);
    if (Objects.isNull(account))
      return getBaseResponse(String.format("account with id %d does not exist", id), STATUS_NOT_FOUND());

    if (accountHelper.isPresent(request.getUsername())
        && !accountHelper.isUsernameEquals(request.getUsername(), account.getUsername()))
      return getBaseResponse(String.format("username is taken: %s", request.getUsername()),
          STATUS_CONFLICT());

    Account updatedAccount = new Account();
    BeanUtils.copyProperties(account, updatedAccount);
    BeanUtils.copyProperties(request, updatedAccount);

    Account savedAccount = accountRepository.save(updatedAccount);

    return getBaseResponse(toAccountResponse(savedAccount), STATUS_OK());
  }

  @Override
  public BaseResponse<?> deleteById(Long id) {

    if (Objects.isNull(id))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    Account account = accountHelper.findOneAccount(id);

    if (Objects.isNull(account))
      return getBaseResponse(String.format("account with id %d does not exist", id), STATUS_NOT_FOUND());

    accountRepository.deleteById(id);

    return getBaseResponse(true, STATUS_OK());
  }

  @Override
  public BaseResponse<?> follow(CreateAccountRelationshipRequest request) {

    if (Objects.isNull(request))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    Account follower = accountHelper.findOneAccount(request.getFollowerId());
    if (Objects.isNull(follower))
      return getBaseResponse(String.format("account with id %d does not exist", request.getFollowerId()),
          STATUS_UNPROCESSABLE());

    Account followed = accountHelper.findOneAccount(request.getFollowedId());
    if (Objects.isNull(followed))
      return getBaseResponse(String.format("account with id %d does not exist", request.getFollowedId()),
          STATUS_UNPROCESSABLE());

    AccountRelationship followData = new AccountRelationship();
    followData.setFollower(follower);
    followData.setFollowed(followed);
    BeanUtils.copyProperties(request, followData);

    AccountRelationship newFollowData = accountRelationshipRepository.save(followData);

    return getBaseResponse(toAccountRelationshipResponse(newFollowData), STATUS_OK());
  }

  @Override
  public BaseResponse<?> unfollow(CreateAccountRelationshipRequest request) {

    if (Objects.isNull(request))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    AccountRelationship accountRelationship = accountHelper.findOneRelationship(request);
    if (Objects.isNull(accountRelationship))
      return getBaseResponse(String.format("relationship does not exist"), STATUS_UNPROCESSABLE());

    accountRelationshipRepository.delete(accountRelationship);

    return getBaseResponse(true, STATUS_OK());
  }

  private AccountResponse toAccountResponse(Account account) {

    AccountResponse accountResponse = new AccountResponse();
    if (Objects.isNull(account))
      return null;

    BeanUtils.copyProperties(account, accountResponse);
    return accountResponse;
  }

  private Account saveOneAccount(CreateAccountRequest request) {

    Account newAccount = new Account();
    BeanUtils.copyProperties(request, newAccount);

    return accountRepository.save(newAccount);
  }

  private Account toAccount(CreateAccountRequest request) {

    Account account = new Account();
    BeanUtils.copyProperties(request, account);
    return account;
  }

  private AccountRelationshipResponse toAccountRelationshipResponse(AccountRelationship relationData) {

    AccountRelationshipResponse response = new AccountRelationshipResponse();
    if (Objects.isNull(relationData))
      return null;

    response.setFollowerId(relationData.getFollower()
        .getId());
    response.setFollowedId(relationData.getFollowed()
        .getId());
    BeanUtils.copyProperties(relationData, response);

    return response;
  }
}
