package com.vincent.mutualan.mutualankuy.service;

import java.util.List;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.account.AccountResponse;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.CreateAccountRelationshipRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

  BaseResponse<?> createOne(CreateAccountRequest request);

  BaseResponse<?> createMany(List<CreateAccountRequest> requests);

  BaseResponse<List<AccountResponse>> findAll();

  BaseResponse<?> findById(Long id);

  BaseResponse<?> updateOne(Long id, UpdateAccountRequest request);

  BaseResponse<?> deleteById(Long id);

  BaseResponse<?> follow(CreateAccountRelationshipRequest request);

  BaseResponse<?> unfollow(CreateAccountRelationshipRequest request);
}
