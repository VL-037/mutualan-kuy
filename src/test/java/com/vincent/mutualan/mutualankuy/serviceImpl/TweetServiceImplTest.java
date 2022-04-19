package com.vincent.mutualan.mutualankuy.serviceImpl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NO_CONTENT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.units.qual.C;
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
  void createOne_success() {

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    Tweet expected = new Tweet();
    expected.setMessage("tweet #1");
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

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    Tweet expected = new Tweet();
    expected.setMessage("tweet #1");
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

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    Assertions.assertThat(tweetService.createOne(creator.getId(), null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(tweetRepository, never())
        .save(any());
  }

  @Test
  void createMany_success() {

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    List<Tweet> tweetList = new ArrayList<>();
    List<CreateTweetRequest> requestList = new ArrayList<>();

    Tweet tweet1 = new Tweet();
    tweet1.setMessage("tweet #1");

    tweetList.add(tweet1);
    CreateTweetRequest request1 = new CreateTweetRequest();
    BeanUtils.copyProperties(tweet1, request1);
    requestList.add(request1);

    Tweet tweet2 = new Tweet();
    tweet2.setMessage("tweet #2");

    tweetList.add(tweet2);
    CreateTweetRequest request2 = new CreateTweetRequest();
    BeanUtils.copyProperties(tweet2, request2);
    requestList.add(request2);

    Tweet tweet3 = new Tweet();
    tweet3.setMessage("tweet #3");

    tweetList.add(tweet3);
    CreateTweetRequest request3 = new CreateTweetRequest();
    BeanUtils.copyProperties(tweet3, request3);
    requestList.add(request3);

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

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    Assertions.assertThat(tweetService.createMany(creator.getId(), null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(tweetRepository, never())
        .saveAll(any());
  }

  @Test
  void createMany_whenAccountExistsAndRequestIsEmptyList_shouldReturnStatusNoContentAndNeverSaved() {

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

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

    Account creator = new Account();
    creator.setId(1L);
    creator.setFirstName("vincent");
    creator.setLastName("low");
    creator.setBirthDate(LocalDate.MIN);
    creator.setUsername("vincent.low");

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(tweetService.createMany(creator.getId(), new ArrayList<>())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(tweetRepository, never())
        .findById(any());
  }

  @Test
  void updateOne() {

  }

  @Test
  void deleteOne() {}
}
