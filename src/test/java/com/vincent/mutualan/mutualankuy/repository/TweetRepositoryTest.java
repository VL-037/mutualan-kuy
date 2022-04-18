package com.vincent.mutualan.mutualankuy.repository;

import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.entity.Tweet;

@DataJpaTest
class TweetRepositoryTest {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private TweetRepository tweetRepository;

  @AfterEach
  void tearDown() {

    tweetRepository.deleteAll();
    accountRepository.deleteAll();
  }

  @Test
  void findAllByAccountId_whenAccountAndTweetsExists_shouldReturnNotEmptyList() {

    Account creator = new Account();
    creator.setFirstName("Vincent");
    creator.setLastName("Low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    Account savedCreator = accountRepository.save(creator);

    List<Tweet> tweetList = new ArrayList<>();

    Tweet tweet1 = new Tweet();
    tweet1.setId(anyLong());
    tweet1.setMessage("tweet #1");
    tweet1.setCreator(savedCreator);

    Tweet tweet2 = new Tweet();
    tweet2.setId(anyLong());
    tweet2.setMessage("tweet #2");
    tweet2.setCreator(savedCreator);

    Tweet tweet3 = new Tweet();
    tweet3.setId(anyLong());
    tweet3.setMessage("tweet #3");
    tweet3.setCreator(savedCreator);

    tweetList.add(tweet1);
    tweetList.add(tweet2);
    tweetList.add(tweet3);

    tweetRepository.save(tweet1);
    tweetRepository.save(tweet2);
    tweetRepository.save(tweet3);
    savedCreator.getTweets()
        .addAll(tweetList);

    List<Tweet> result = tweetRepository.findAllByAccountId(savedCreator.getId())
        .stream()
        .findFirst()
        .orElse(null);

    Assertions.assertThat(result.size() > 0)
        .isTrue();
    Assertions.assertThat(result.size())
        .isEqualTo(tweetList.size());
  }

  @Test
  void findAllByAccountId_whenIdDoesNotExists_shouldReturnEmptyList() {

    List<Tweet> result = tweetRepository.findAllByAccountId(anyLong())
        .stream()
        .findFirst()
        .orElse(null);

    Assertions.assertThat(result.size())
        .isEqualTo(0);
  }

  @Test
  void findByTweetId_whenAccountAndTweetExists_shouldReturnNotNull() {

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("Vincent");
    creator.setLastName("Low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");
    creator.setTweets(new ArrayList<>());

    Tweet expected = new Tweet();
    expected.setId(1L);
    expected.setMessage("tweet #1");
    expected.setCreator(creator);

    accountRepository.save(creator);
    tweetRepository.save(expected);

    Tweet result = tweetRepository.findByTweetId(creator.getId(), expected.getId())
        .stream()
        .findFirst()
        .orElse(null);

    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  void findByTweetId_whenAccountDoesNotExists_shouldReturnNull() {

    Tweet expected = new Tweet();
    expected.setId(1L);
    expected.setMessage("tweet #1");
    expected.setCreator(null);

    Tweet result = tweetRepository.findByTweetId(anyLong(), expected.getId())
        .stream()
        .findFirst()
        .orElse(null);

    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(null);
  }

  @Test
  void findByTweetId_whenTweetDoesNotExists_shouldReturnNull() {

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("Vincent");
    creator.setLastName("Low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");
    creator.setTweets(new ArrayList<>());

    Tweet result = tweetRepository.findByTweetId(creator.getId(), anyLong())
        .stream()
        .findFirst()
        .orElse(null);

    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(null);
  }
}
