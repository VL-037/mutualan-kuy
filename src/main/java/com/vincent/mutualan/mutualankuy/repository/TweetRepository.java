package com.vincent.mutualan.mutualankuy.repository;

import java.util.List;
import java.util.Optional;

import com.vincent.mutualan.mutualankuy.model.tweet.UpdateTweetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vincent.mutualan.mutualankuy.entity.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

  @Query("SELECT t FROM Tweet t WHERE t.creator=(SELECT a FROM Account a WHERE a.id=?1)")
  Optional<List<Tweet>> findAllByAccountId(Long accountId);

  @Query("SELECT t FROM Tweet t WHERE t.creator=(SELECT a FROM Account a WHERE a.id=?1) AND t.id=?2")
  Optional<Tweet> findByTweetId(Long accountId, Long tweetId);
}
