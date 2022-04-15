package com.vincent.mutualan.mutualankuy.service.impl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.getBaseResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.entity.Tweet;
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.TweetResponse;
import com.vincent.mutualan.mutualankuy.repository.TweetRepository;
import com.vincent.mutualan.mutualankuy.service.TweetService;

@Service
public class TweetServiceImpl implements TweetService {

  @Autowired
  private TweetRepository tweetRepository;

  @Autowired
  private AccountHelper accountHelper;

  @Override
  public BaseResponse<List<TweetResponse>> findAllByAccountId(Long accountId) {

    accountHelper.findOneAccount(accountId);

    List<Tweet> tweets = tweetRepository.findAllByAccountId(accountId);
    List<TweetResponse> tweetResponses = tweets.stream()
        .map(this::toTweetResponse)
        .collect(Collectors.toList());

    return getBaseResponse(tweetResponses, STATUS_OK());
  }

  @Override
  public BaseResponse<TweetResponse> createOne(Long accountId, CreateTweetRequest request) {

    Account creator = accountHelper.findOneAccount(accountId);
    Tweet newTweet = saveOneTweet(creator, request);

    return getBaseResponse(toTweetResponse(newTweet), STATUS_OK());
  }

  @Override
  public BaseResponse<List<TweetResponse>> createMany(Long accountId, List<CreateTweetRequest> requests) {

    Account creator = accountHelper.findOneAccount(accountId);

    List<TweetResponse> tweetResponses = requests.stream()
        .map(request -> saveOneTweet(creator, request))
        .collect(Collectors.toList())
        .stream()
        .map(this::toTweetResponse)
        .collect(Collectors.toList());

    return getBaseResponse(tweetResponses, STATUS_OK());
  }

  private TweetResponse toTweetResponse(Tweet tweet) {

    TweetResponse tweetResponse = new TweetResponse();
    tweetResponse.setCreatorId(tweet.getCreator()
        .getId());
    BeanUtils.copyProperties(tweet, tweetResponse);
    return tweetResponse;
  }

  private Tweet saveOneTweet(Account creator, CreateTweetRequest request) {

    Tweet newTweet = new Tweet();
    newTweet.setCreator(creator);
    BeanUtils.copyProperties(request, newTweet);

    return tweetRepository.save(newTweet);
  }
}
