package com.vincent.mutualan.mutualankuy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.TweetResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.UpdateTweetRequest;

@Service
public interface TweetService {

    BaseResponse<?> findAllByAccountId(Long accountId);

    BaseResponse<?> findOneByTweetId(Long accountId, Long tweetId);

    BaseResponse<?> createOne(Long accountId, CreateTweetRequest request);

    BaseResponse<?> createMany(Long accountId, List<CreateTweetRequest> requests);

    BaseResponse<?> updateOne(Long accountId, Long tweetId, UpdateTweetRequest request);

    BaseResponse<?> deleteOne(Long accountId, Long tweetId);
}
