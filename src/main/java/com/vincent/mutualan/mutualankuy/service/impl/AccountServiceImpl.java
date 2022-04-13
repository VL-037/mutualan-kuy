package com.vincent.mutualan.mutualankuy.service.impl;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.entity.Follower;
import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.account.AccountResponse;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.repository.AccountRepository;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;
import com.vincent.mutualan.mutualankuy.repository.FollowersRepository;
import com.vincent.mutualan.mutualankuy.service.AccountService;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private FollowersRepository followersRepository;

  @Override
  public BaseResponse<AccountResponse> createOne(CreateAccountRequest request) {

    if (isPresent(request.getUsername()))
      throw new IllegalStateException("username taken");

    Account newAccount = saveOneAccount(request);

    return BaseResponse.<AccountResponse>builder()
        .status(HttpStatus.OK.value())
        .data(toAccountResponse(newAccount))
        .build();
  }

  @Override
  public BaseResponse<List<AccountResponse>> createMany(List<CreateAccountRequest> requests) {

    List<AccountResponse> accountResponses = requests.stream()
        .map(this::saveOneAccount)
        .collect(Collectors.toList())
        .stream()
        .map(this::toAccountResponse)
        .collect(Collectors.toList());

    return BaseResponse.<List<AccountResponse>>builder()
        .status(HttpStatus.OK.value())
        .data(accountResponses)
        .build();
  }

  @Override
  public BaseResponse<List<AccountResponse>> findAll() {

    List<Account> accounts = accountRepository.findAll();
    List<AccountResponse> accountResponses = accounts.stream()
        .map(this::toAccountResponse)
        .collect(Collectors.toList());

    return BaseResponse.<List<AccountResponse>>builder()
        .status(HttpStatus.OK.value())
        .data(accountResponses)
        .build();
  }

  @Override
  public BaseResponse<AccountResponse> findById(Long id) {

    Account account = accountRepository.findById(id)
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(String.format("account with id %d does not exist", id)));

    return BaseResponse.<AccountResponse>builder()
        .status(HttpStatus.OK.value())
        .data(toAccountResponse(account))
        .build();
  }

  @Override
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public BaseResponse<AccountResponse> updateOne(Long id, UpdateAccountRequest request) {

    Account account = accountRepository.findById(id)
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(String.format("account with id %d does not exist", id)));

    if (isPresent(request.getUsername()) && !isUsernameEquals(request, account))
      throw new IllegalStateException(String.format("username already exists"));

    account.setFirstName(request.getFirstName());

    if (Strings.isNotBlank(request.getMiddleName()))
      account.setMiddleName(request.getMiddleName());
    if (Strings.isNotBlank(request.getBio()))
      account.setBio(request.getBio());

    account.setUpdatedAt(new Date());

    return BaseResponse.<AccountResponse>builder()
        .status(HttpStatus.OK.value())
        .data(toAccountResponse(account))
        .build();
  }

  @Override
  public BaseResponse<Boolean> deleteById(Long id) {

    if (!accountRepository.existsById(id))
      throw new IllegalStateException(String.format("account with id: %d does not exist", id));

    accountRepository.deleteById(id);

    return BaseResponse.<Boolean>builder()
        .status(HttpStatus.OK.value())
        .data(true)
        .build();
  }

  @Override
  public BaseResponse<Boolean> follow(Long to_user_id) {

    Follower followData = Follower.builder()
        .from_user_id(null)
        .to_user_id(null)
        .build();

    followersRepository.save(followData);

    return BaseResponse.<Boolean>builder()
        .status(HttpStatus.OK.value())
        .data(true)
        .build();
  }

  @Override
  public BaseResponse<Boolean> unfollow(Long to_user_id) {

    return BaseResponse.<Boolean>builder()
        .status(HttpStatus.OK.value())
        .data(true)
        .build();
  }

  private AccountResponse toAccountResponse(Account account) {

    AccountResponse accountResponse = new AccountResponse();
    BeanUtils.copyProperties(account, accountResponse);
    return accountResponse;
  }

  private Account saveOneAccount(CreateAccountRequest request) {

    Account newAccount = new Account();
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
}
