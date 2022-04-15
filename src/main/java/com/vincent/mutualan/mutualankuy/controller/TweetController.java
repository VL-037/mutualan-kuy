package com.vincent.mutualan.mutualankuy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.TweetResponse;
import com.vincent.mutualan.mutualankuy.service.TweetService;

@RestController
@RequestMapping("api/v1/accounts/{id}/tweets")
public class TweetController {

  @Autowired
  private TweetService tweetService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<TweetResponse>> getAllTweetsOfOneAccount(@PathVariable Long id) {

    return tweetService.findAllByAccountId(id);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<TweetResponse> createOneTweet(@PathVariable Long id, @RequestBody CreateTweetRequest request) {

    return tweetService.createOne(id, request);
  }

  @PostMapping(path = "/many", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<TweetResponse>> createManyTweet(@PathVariable Long id,
          @RequestBody List<CreateTweetRequest> requests) {

    return tweetService.createMany(id, requests);
  }
}
