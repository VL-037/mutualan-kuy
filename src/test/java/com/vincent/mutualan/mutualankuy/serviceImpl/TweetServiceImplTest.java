package com.vincent.mutualan.mutualankuy.serviceImpl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vincent.mutualan.mutualankuy.entity.Tweet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.helper.tweet.TweetHelper;
import com.vincent.mutualan.mutualankuy.repository.TweetRepository;
import com.vincent.mutualan.mutualankuy.service.TweetService;
import com.vincent.mutualan.mutualankuy.service.impl.TweetServiceImpl;

@ExtendWith(MockitoExtension.class)
class TweetServiceImplTest {

  @Mock
  private TweetRepository tweetRepository;

  @Mock
  private TweetHelper tweetHelper;

  @Mock
  private AccountHelper accountHelper;

  private TweetService tweetService;

  @BeforeEach
  void setUp() {

    tweetService = new TweetServiceImpl(tweetRepository, accountHelper, tweetHelper);
  }

  @Test
  void findAllByAccountId_whenAccountExistsAndTweetListIsNotNull_success() {

    Account account = new Account();
    account.setId(1L);
    account.getTweets()
        .addAll(new ArrayList<>());

    BDDMockito.given(accountHelper.findOneAccount(account.getId()))
        .willReturn(account);
    tweetService.findAllByAccountId(account.getId());

    Mockito.verify(tweetRepository)
        .findAllByAccountId(account.getId());
  }

  @Test
  void findAllByAccountId_whenAccountExistsAndTweetListIsEmpty_shouldReturnStatusOkAndEmptyTweetList() {

    Account account = new Account();
    account.setId(1L);
    account.setTweets(new ArrayList<>());

    BDDMockito.given(accountHelper.findOneAccount(account.getId()))
        .willReturn(account);
    BDDMockito.given(tweetRepository.findAllByAccountId(account.getId()))
        .willReturn(Optional.of(new ArrayList<>()));

    tweetService.findAllByAccountId(account.getId());

    Assertions.assertThat(tweetService.findAllByAccountId(account.getId())
        .getStatus())
        .isEqualTo(STATUS_OK());

    Assertions.assertThat(tweetService.findAllByAccountId(account.getId())
        .getData())
        .isEqualTo(List.of());
  }

  @Test
  void findAllByAccountId_whenAccountDoesNotExist_shouldReturnStatusNotFoundAndNeverFound() {

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.findAllByAccountId(anyLong())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .findAllByAccountId(anyLong());
  }

  @Test
  void findOneByTweetId_whenAccountExistsAndTweetExists_success() {

    Account creator = new Account();
    creator.setId(1L);

    Tweet tweet = new Tweet();
    tweet.setId(1L);
    tweet.setCreator(creator);

    creator.getTweets()
        .add(tweet);

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);

    tweetService.findOneByTweetId(creator.getId(), tweet.getId());

    ArgumentCaptor<Long> tweetIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(accountHelper)
        .findOneAccount(tweetIdArgumentCaptor.capture());

    Long result = tweetIdArgumentCaptor.getValue();
    Assertions.assertThat(result)
        .isEqualTo(tweet.getId());
  }

  @Test
  void findOneByTweetId_whenAccountExistsAndTweetDoesNotExists_shouldReturnStatusNotFound() {

    Account creator = new Account();
    creator.setId(1L);

    Tweet tweet = new Tweet();
    tweet.setId(1L);
    tweet.setCreator(creator);

    creator.getTweets()
        .add(tweet);

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);
    BDDMockito.given(tweetHelper.findOneTweet(creator.getId(), tweet.getId()))
        .willReturn(null);

    Assertions.assertThat(tweetService.findOneByTweetId(creator.getId(), tweet.getId())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());
  }

  @Test
  void findOneByTweetId_whenAccountDoesNotExist_shouldReturnStatusNotFoundAndNeverFound() {

    Account creator = new Account();
    creator.setId(1L);

    Tweet tweet = new Tweet();
    tweet.setId(1L);
    tweet.setCreator(creator);

    creator.getTweets()
        .add(tweet);

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.findOneByTweetId(creator.getId(), tweet.getId())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .findByTweetId(creator.getId(), tweet.getId());
  }

  @Test
  void createOne() {}

  @Test
  void createMany() {}

  @Test
  void updateOne() {}

  @Test
  void deleteOne() {}
}
