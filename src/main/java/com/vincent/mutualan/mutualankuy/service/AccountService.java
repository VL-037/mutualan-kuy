package com.vincent.mutualan.mutualankuy.service;

import java.util.List;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.account.AccountResponse;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;

public interface AccountService {

    BaseResponse<AccountResponse> createOne(CreateAccountRequest request);

    BaseResponse<List<AccountResponse>> createMany(List<CreateAccountRequest> requests);

    BaseResponse<List<AccountResponse>> findAll();

    BaseResponse<AccountResponse> findById(Long id);

    BaseResponse<AccountResponse> updateOne(Long id, UpdateAccountRequest request);

    BaseResponse<Boolean> deleteById(Long id);

    BaseResponse<Boolean> follow(Long to_user_id);

    BaseResponse<Boolean> unfollow(Long to_user_id);
}
