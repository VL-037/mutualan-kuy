package com.vincent.mutualan.mutualankuy.service.impl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NO_CONTENT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.getBaseResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.entity.Tweet;
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.helper.tweet.TweetHelper;
import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.TweetResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.UpdateTweetRequest;
import com.vincent.mutualan.mutualankuy.repository.TweetRepository;
import com.vincent.mutualan.mutualankuy.service.TweetService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {

  @Autowired
  private TweetRepository tweetRepository;

  @Autowired
  private AccountHelper accountHelper;

  @Autowired
  private TweetHelper tweetHelper;

  @Override
  public BaseResponse<?> findAllByAccountId(Long accountId) {

    Account account = accountHelper.findOneAccount(accountId);
    if (Objects.isNull(account))
      return getBaseResponse(String.format("account with id %d does not exist", accountId), STATUS_NOT_FOUND());

    List<Tweet> tweets = tweetRepository.findAllByAccountId(accountId)
        .stream()
        .findFirst()
        .orElse(new ArrayList<>());

    List<TweetResponse> tweetResponses = tweets.stream()
        .map(this::toTweetResponse)
        .collect(Collectors.toList());

    return getBaseResponse(tweetResponses, STATUS_OK());
  }

  @Override
  public BaseResponse<?> findOneByTweetId(Long accountId, Long tweetId) {

    Account account = accountHelper.findOneAccount(accountId);
    if (Objects.isNull(account))
      return getBaseResponse(String.format("account with id %d does not exist", accountId), STATUS_NOT_FOUND());

    Tweet tweet = tweetHelper.findOneTweet(accountId, tweetId);
    if (Objects.isNull(tweet))
      return getBaseResponse(String.format("tweet with id %d does not exist", tweetId), STATUS_NOT_FOUND());

    return getBaseResponse(toTweetResponse(tweet), STATUS_OK());
  }

  @Override
  public BaseResponse<?> createOne(Long accountId, CreateTweetRequest request) {

    if (Objects.isNull(request))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    Account creator = accountHelper.findOneAccount(accountId);
    if (Objects.isNull(creator))
      return getBaseResponse(String.format("account with id %d does not exist", accountId), STATUS_NOT_FOUND());

    Tweet newTweet = saveOneTweet(creator, request);

    return getBaseResponse(toTweetResponse(newTweet), STATUS_OK());
  }

  @Override
  public BaseResponse<?> createMany(Long accountId, List<CreateTweetRequest> requests) {

    if (Objects.isNull(requests))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    Account creator = accountHelper.findOneAccount(accountId);
    if (Objects.isNull(creator))
      return getBaseResponse(String.format("account with id %d does not exist", accountId), STATUS_NOT_FOUND());

    List<Tweet> newTweets = requests.stream()
        .map(this::toTweet)
        .collect(Collectors.toList());

    if (newTweets.size() == 0)
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    List<Tweet> savedTweets = tweetRepository.saveAll(newTweets);

    List<TweetResponse> tweetResponses = savedTweets.stream()
        .map(this::toTweetResponse)
        .collect(Collectors.toList());

    return getBaseResponse(tweetResponses, STATUS_OK());
  }

  private Tweet toTweet(CreateTweetRequest request) {

    Tweet tweet = new Tweet();
    BeanUtils.copyProperties(request, tweet);
    return tweet;
  }

  @Override
  public BaseResponse<?> updateOne(Long accountId, Long tweetId, UpdateTweetRequest request) {

    if (Objects.isNull(request))
      return getBaseResponse(String.format("empty request body"), STATUS_NO_CONTENT());

    Account creator = accountHelper.findOneAccount(accountId);
    if (Objects.isNull(creator))
      return getBaseResponse(String.format("account with id %d does not exist", accountId), STATUS_NOT_FOUND());

    Tweet tweet = tweetHelper.findOneTweet(accountId, tweetId);
    if (Objects.isNull(tweet))
      return getBaseResponse(String.format("tweet with id %d does not exist", tweetId), STATUS_NOT_FOUND());

    tweet.setMessage(request.getMessage());
    tweet.setUpdatedAt(new Date());

    Tweet savedTweet = tweetRepository.save(tweet);

    return getBaseResponse(toTweetResponse(savedTweet), STATUS_OK());
  }

  @Override
  public BaseResponse<?> deleteOne(Long accountId, Long tweetId) {

    Account creator = accountHelper.findOneAccount(accountId);
    if (Objects.isNull(creator))
      return getBaseResponse(String.format("account with id %d does not exist", accountId), STATUS_NOT_FOUND());

    Tweet tweet = tweetHelper.findOneTweet(accountId, tweetId);
    if (Objects.isNull(tweet))
      return getBaseResponse(String.format("tweet with id %d does not exist", tweetId), STATUS_NOT_FOUND());

    tweetRepository.delete(tweet);

    return getBaseResponse(true, STATUS_OK());
  }

  private TweetResponse toTweetResponse(Tweet tweet) {

    if (Objects.isNull(tweet))
      return null;

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

    Tweet saved = tweetRepository.save(newTweet);

    return saved;
  }
}
