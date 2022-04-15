package com.vincent.mutualan.mutualankuy.helper.tweet;

import com.vincent.mutualan.mutualankuy.entity.Tweet;
import com.vincent.mutualan.mutualankuy.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TweetHelper {

    @Autowired
    private TweetRepository tweetRepository;

    public Tweet findOneTweet(Long accountId, Long tweetId) {
        return tweetRepository.findByTweetId(accountId, tweetId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("tweet with id %d does not exist", tweetId)));
    }
}
