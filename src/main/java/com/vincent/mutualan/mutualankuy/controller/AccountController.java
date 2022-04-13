package com.vincent.mutualan.mutualankuy.controller;

import java.util.List;

import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.account.AccountResponse;
import com.vincent.mutualan.mutualankuy.service.impl.AccountServiceImpl;

@RestController
@RequestMapping(path = "api/v1/accounts")
public class AccountController {

  @Autowired
  private AccountServiceImpl accountServiceImpl;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<AccountResponse>> getAccounts() {

    return accountServiceImpl.findAll();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<AccountResponse> registerNewAccount(@RequestBody CreateAccountRequest request) {

    return accountServiceImpl.createOne(request);
  }

  @PostMapping(path = "/many", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<AccountResponse>> registerManyNewAccount(@RequestBody List<CreateAccountRequest> requests) {

    return accountServiceImpl.createMany(requests);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<AccountResponse> getOneAccount(@PathVariable Long id) {

    return accountServiceImpl.findById(id);
  }

  @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<AccountResponse> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {

    return accountServiceImpl.updateOne(id, request);
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Boolean> deleteAccount(@PathVariable Long id) {

    return accountServiceImpl.deleteById(id);
  }

  @PostMapping(path = "/{to_user_id}/follow", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Boolean> followAccount(@PathVariable Long to_user_id) {

    return accountServiceImpl.follow(to_user_id);
  }

  @DeleteMapping(path = "/{to_user_id}/unfollow", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<Boolean> unfollowAccount(@PathVariable Long to_user_id) {

    return accountServiceImpl.unfollow(to_user_id);
  }
}
