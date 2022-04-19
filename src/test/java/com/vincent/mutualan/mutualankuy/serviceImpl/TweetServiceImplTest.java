package com.vincent.mutualan.mutualankuy.serviceImpl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NO_CONTENT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import com.vincent.mutualan.mutualankuy.entity.Account;
import com.vincent.mutualan.mutualankuy.entity.Tweet;
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.helper.tweet.TweetHelper;
import com.vincent.mutualan.mutualankuy.model.tweet.CreateTweetRequest;
import com.vincent.mutualan.mutualankuy.model.tweet.UpdateTweetRequest;
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

  private Account initOneAccount() {

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    return creator;
  }

  private Tweet initOneTweet() {

    Tweet tweet = new Tweet();
    tweet.setId(1L);

    tweet.setMessage("tweet #1");
    return tweet;
  }

  private List<Tweet> initManyTweets() {

    List<Tweet> tweetList = new ArrayList<>();

    Tweet tweet1 = new Tweet();
    tweet1.setMessage("tweet #1");

    Tweet tweet2 = new Tweet();
    tweet1.setMessage("tweet #1");

    Tweet tweet3 = new Tweet();
    tweet1.setMessage("tweet #1");

    tweetList.add(tweet1);
    tweetList.add(tweet2);
    tweetList.add(tweet3);

    return tweetList;
  }

  @Test
  void findAllByAccountId_whenAccountExistsAndTweetListIsIsEmptyList_success() {

    Account account = initOneAccount();
    account.setTweets(new ArrayList<>());

    BDDMockito.given(accountHelper.findOneAccount(account.getId()))
        .willReturn(account);
    tweetService.findAllByAccountId(account.getId());

    Mockito.verify(tweetRepository)
        .findAllByAccountId(account.getId());

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

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
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

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
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

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
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
  void createOne_success() {

    Account creator = initOneAccount();

    Tweet expected = initOneTweet();
    expected.setId(null);
    expected.setCreator(creator);

    CreateTweetRequest request = new CreateTweetRequest();
    BeanUtils.copyProperties(expected, request);

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);

    tweetService.createOne(creator.getId(), request);

    ArgumentCaptor<Tweet> tweetArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);
    Mockito.verify(tweetRepository)
        .save(tweetArgumentCaptor.capture());

    Tweet result = tweetArgumentCaptor.getValue();
    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expected);
  }

  @Test
  void createOne_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeverSaved() {

    Account creator = initOneAccount();

    Tweet expected = initOneTweet();
    expected.setCreator(creator);

    CreateTweetRequest request = new CreateTweetRequest();
    BeanUtils.copyProperties(expected, request);

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.createOne(creator.getId(), request)
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .save(expected);
  }

  @Test
  void createOne_whenAccountExistsAndRequestIsNull_shouldReturnStatusNoContentAndNeverSaved() {

    Account creator = initOneAccount();

    Assertions.assertThat(tweetService.createOne(creator.getId(), null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(tweetRepository, never())
        .save(any());
  }

  @Test
  void createMany_success() {

    Account creator = initOneAccount();

    List<Tweet> tweetList = initManyTweets();
    List<CreateTweetRequest> requestList = new ArrayList<>();

    for (int i = 0; i < tweetList.size(); i++) {
      CreateTweetRequest request = new CreateTweetRequest();
      BeanUtils.copyProperties(tweetList.get(i), request);
      requestList.add(request);
    }

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);

    tweetService.createMany(creator.getId(), requestList);

    ArgumentCaptor<List<Tweet>> listTweetCaptor = ArgumentCaptor.forClass(List.class);
    Mockito.verify(tweetRepository)
        .saveAll(listTweetCaptor.capture());

    List<Tweet> capturedTweets = listTweetCaptor.getValue();

    for (int i = 0; i < requestList.size(); i++) {
      Assertions.assertThat(capturedTweets.get(i))
          .usingRecursiveComparison()
          .isEqualTo(tweetList.get(i));
    }
  }

  @Test
  void createMany_whenAccountExistsAndRequestIsNull_shouldReturnStatusNoContentAndNeverSaved() {

    Account creator = initOneAccount();

    Assertions.assertThat(tweetService.createMany(creator.getId(), null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(tweetRepository, never())
        .saveAll(any());
  }

  @Test
  void createMany_whenAccountExistsAndRequestIsEmptyList_shouldReturnStatusNoContentAndNeverSaved() {

    Account creator = initOneAccount();

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);

    Assertions.assertThat(tweetService.createMany(creator.getId(), new ArrayList<>())
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(tweetRepository, never())
        .saveAll(any());
  }

  @Test
  void createMany_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeverSaved() {

    Account creator = initOneAccount();

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.createMany(creator.getId(), new ArrayList<>())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .findById(any());
  }

  @Test
  void updateOne_whenAccountExistsAndTweetExists_success() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    UpdateTweetRequest request = new UpdateTweetRequest();
    request.setMessage("tweet #1 updated");

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);
    BDDMockito.given(tweetHelper.findOneTweet(creator.getId(), tweet.getId()))
        .willReturn(tweet);

    tweetService.updateOne(creator.getId(), tweet.getId(), request);

    ArgumentCaptor<Tweet> tweetArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);
    Mockito.verify(tweetRepository)
        .save(tweetArgumentCaptor.capture());

    Tweet result = tweetArgumentCaptor.getValue();
    Assertions.assertThat(result.getId())
        .isEqualTo(tweet.getId());

    Assertions.assertThat(result.getMessage())
        .isEqualTo(request.getMessage());

    Mockito.verify(tweetRepository)
        .save(any());
  }

  @Test
  void updateOne_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeverSaved() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    UpdateTweetRequest request = new UpdateTweetRequest();
    request.setMessage("tweet #1 updated");

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.updateOne(creator.getId(), tweet.getId(), request)
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .save(any());
  }

  @Test
  void updateOne_whenAccountExistsAndTweetDoesNotExists_shouldReturnStatusNotFoundAndNeverSaved() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    UpdateTweetRequest request = new UpdateTweetRequest();
    request.setMessage("tweet #1 updated");

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);
    BDDMockito.given(tweetHelper.findOneTweet(creator.getId(), tweet.getId()))
        .willReturn(null);

    Assertions.assertThat(tweetService.updateOne(creator.getId(), tweet.getId(), request)
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .save(any());
  }

  @Test
  void updateOne_whenAccountExistsAndRequestIsNull_shouldReturnNoContentAndNeverSaved() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    Assertions.assertThat(tweetService.updateOne(creator.getId(), tweet.getId(), null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(tweetRepository, never())
        .save(any());
  }

  @Test
  void deleteOne_whenAccountExistsAndTweetExists_success() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);
    BDDMockito.given(tweetHelper.findOneTweet(creator.getId(), tweet.getId()))
        .willReturn(tweet);

    tweetService.deleteOne(creator.getId(), tweet.getId());

    ArgumentCaptor<Tweet> tweetArgumentCaptor = ArgumentCaptor.forClass(Tweet.class);
    Mockito.verify(tweetRepository)
        .delete(tweetArgumentCaptor.capture());

    Tweet capturedTweet = tweetArgumentCaptor.getValue();
    Assertions.assertThat(capturedTweet.getId())
        .isEqualTo(tweet.getId());
  }

  @Test
  void deleteOne_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeveDeleted() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.deleteOne(creator.getId(), tweet.getId())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .delete(any());
  }

  @Test
  void deleteOne_whenAccountExistsAndTweetDoesNotExist_shouldReturnStatusNotFoundAndNeverDeleted() {

    Account creator = initOneAccount();

    Tweet tweet = initOneTweet();
    tweet.setCreator(creator);

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);
    BDDMockito.given(tweetHelper.findOneTweet(creator.getId(), tweet.getId()))
        .willReturn(null);

    Assertions.assertThat(tweetService.deleteOne(creator.getId(), tweet.getId())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .delete(any());
  }

  @Test
  void deleteOne_whenAccountExistsAndRequestIsNull_shouldReturnStatusNotFoundAndNeverSaved() {

    Account creator = initOneAccount();

    BDDMockito.given(accountHelper.findOneAccount(creator.getId()))
        .willReturn(creator);

    Assertions.assertThat(tweetService.deleteOne(creator.getId(), null)
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .delete(any());
  }
}
