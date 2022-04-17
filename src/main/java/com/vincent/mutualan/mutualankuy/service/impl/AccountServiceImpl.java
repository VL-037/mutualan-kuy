package com.vincent.mutualan.mutualankuy.service.impl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_CONFLICT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_UNPROCESSABLE;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.getBaseResponse;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
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

    if (isPresent(request.getUsername()))
      return getBaseResponse(String.format("username is taken: %s", request.getUsername()), STATUS_CONFLICT());

    Account newAccount = saveOneAccount(request);

    return getBaseResponse(toAccountResponse(newAccount), STATUS_OK());
  }

  @Override
  public BaseResponse<?> createMany(List<CreateAccountRequest> requests) {

    for (CreateAccountRequest request : requests) {
      if (isPresent(request.getUsername()))
        return getBaseResponse(String.format("username is taken: %s", request.getUsername()), STATUS_CONFLICT());
    }

    List<AccountResponse> accountResponses = requests.stream()
        .map(this::saveOneAccount)
        .collect(Collectors.toList())
        .stream()
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

    if (isPresent(request.getUsername()) && !isUsernameEquals(request, account))
      return getBaseResponse(String.format("username is taken: %s", request.getUsername()), STATUS_CONFLICT());

    account.setFirstName(request.getFirstName());

    if (Strings.isNotBlank(request.getMiddleName()))
      account.setMiddleName(request.getMiddleName());
    if (Strings.isNotBlank(request.getBio()))
      account.setBio(request.getBio());

    account.setUpdatedAt(new Date());

    Account savedAccount = accountRepository.save(account);

    return getBaseResponse(toAccountResponse(savedAccount), STATUS_OK());
  }

  @Override
  public BaseResponse<?> deleteById(Long id) {

    Account account = accountHelper.findOneAccount(id);

    if (Objects.isNull(account))
      return getBaseResponse(String.format("account with id %d does not exist", id), STATUS_NOT_FOUND());

    accountRepository.deleteById(id);

    return getBaseResponse(true, STATUS_OK());
  }

  @Override
  public BaseResponse<?> follow(CreateAccountRelationshipRequest request) {

    Account follower = accountHelper.findOneAccount(request.getFollowerId());
    if (Objects.isNull(follower))
      return getBaseResponse(String.format("account with id %d does not exist", request.getFollowerId()), STATUS_UNPROCESSABLE());

    Account followed = accountHelper.findOneAccount(request.getFollowedId());
    if (Objects.isNull(followed))
      return getBaseResponse(String.format("account with id %d does not exist", request.getFollowedId()), STATUS_UNPROCESSABLE());

    AccountRelationship followData = new AccountRelationship();
    followData.setFollower(follower);
    followData.setFollowed(followed);

    AccountRelationship newFollowData = accountRelationshipRepository.save(followData);

    return getBaseResponse(toAccountRelationshipResponse(newFollowData), STATUS_OK());
  }

  @Override
  public BaseResponse<?> unfollow(CreateAccountRelationshipRequest request) {

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
    if (Objects.isNull(request))
      return null;

    BeanUtils.copyProperties(request, newAccount);

    return accountRepository.save(newAccount);
  }

  private Boolean isPresent(String username) {

    return accountRepository.findOneByUsername(username)
        .isPresent();
  }

  private Boolean isUsernameEquals(UpdateAccountRequest request, Account account) {

    return request.getUsername()
        .equals(account.getUsername());
  }

  private AccountRelationshipResponse toAccountRelationshipResponse(AccountRelationship relationData) {

    AccountRelationshipResponse response = new AccountRelationshipResponse();
    response.setFollowerId(relationData.getFollower()
        .getId());
    response.setFollowedId(relationData.getFollowed()
        .getId());
    BeanUtils.copyProperties(relationData, response);

    return response;
  }
}
