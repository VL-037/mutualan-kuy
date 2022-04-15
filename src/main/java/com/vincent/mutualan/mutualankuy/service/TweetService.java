package com.vincent.mutualan.mutualankuy.service;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.TweetResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TweetService {

    BaseResponse<List<TweetResponse>> findAllByAccountId(Long accountId);

    BaseResponse<TweetResponse> createOne(Long accountId, CreateTweetRequest request);

    BaseResponse<List<TweetResponse>> createMany(Long accountId, List<CreateTweetRequest> requests);
}
