package com.vincent.mutualan.mutualankuy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.TweetResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.UpdateTweetRequest;

@Service
public interface TweetService {

    BaseResponse<List<TweetResponse>> findAllByAccountId(Long accountId);

    BaseResponse<TweetResponse> findOneByTweetId(Long accountId, Long tweetId);

    BaseResponse<TweetResponse> createOne(Long accountId, CreateTweetRequest request);

    BaseResponse<List<TweetResponse>> createMany(Long accountId, List<CreateTweetRequest> requests);

    BaseResponse<TweetResponse> updateOne(Long accountId, Long tweetId, UpdateTweetRequest request);

    BaseResponse<Boolean> deleteOne(Long accountId, Long tweetId);
}
