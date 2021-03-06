package com.vincent.mutualan.mutualankuy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.account.AccountResponse;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.CreateAccountRelationshipRequest;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.AccountRelationshipResponse;
import com.vincent.mutualan.mutualankuy.service.AccountService;

@RestController
@RequestMapping(path = "api/v1/accounts")
public class AccountController {

  @Autowired
  private AccountService accountService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<AccountResponse>> getAccounts() {

    return accountService.findAll();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> registerNewAccount(@RequestBody CreateAccountRequest request) {

    return accountService.createOne(request);
  }

  @PostMapping(path = "/many", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> registerManyNewAccount(@RequestBody List<CreateAccountRequest> requests) {

    return accountService.createMany(requests);
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> getOneAccount(@PathVariable Long id) {

    return accountService.findById(id);
  }

  @PatchMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {

    return accountService.updateOne(id, request);
  }

  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> deleteAccount(@PathVariable Long id) {

    return accountService.deleteById(id);
  }

  @PostMapping(path = "/{followed_id}/follow", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> followAccount(@PathVariable Long followed_id,
      @RequestParam Long follower_id) {

    return accountService.follow(toAccountRelationshipRequest(followed_id, follower_id));
  }

  @PostMapping(path = "/{followed_id}/unfollow", produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<?> unfollowAccount(@PathVariable Long followed_id,
      @RequestParam Long follower_id) {

    return accountService.unfollow(toAccountRelationshipRequest(followed_id, follower_id));
  }

  private CreateAccountRelationshipRequest toAccountRelationshipRequest(Long followed_id, Long follower_id) {

    return CreateAccountRelationshipRequest.builder()
        .followerId(follower_id)
        .followedId(followed_id)
        .build();
  }
}
