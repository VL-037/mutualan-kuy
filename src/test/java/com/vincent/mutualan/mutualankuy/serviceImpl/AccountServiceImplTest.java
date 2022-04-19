package com.vincent.mutualan.mutualankuy.serviceImpl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_CONFLICT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NOT_FOUND;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_NO_CONTENT;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_UNPROCESSABLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.vincent.mutualan.mutualankuy.entity.AccountRelationship;
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.account.UpdateAccountRequest;
import com.vincent.mutualan.mutualankuy.model.accountRelationship.CreateAccountRelationshipRequest;
import com.vincent.mutualan.mutualankuy.repository.AccountRelationshipRepository;
import com.vincent.mutualan.mutualankuy.repository.AccountRepository;
import com.vincent.mutualan.mutualankuy.service.AccountService;
import com.vincent.mutualan.mutualankuy.service.impl.AccountServiceImpl;
import org.springframework.context.annotation.Bean;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AccountRelationshipRepository accountRelationshipRepository;

  @Mock
  private AccountHelper accountHelper;

  private AccountService accountService;

  @BeforeEach
  void setUp() {

    accountService = new AccountServiceImpl(accountRepository, accountRelationshipRepository, accountHelper);
  }

  private Account initOneAccount() {

    Account account = new Account();
    account.setId(1L);
    account.setFirstName("vincent");
    account.setLastName("low");
    account.setBirthDate(LocalDate.MIN);
    account.setUsername("vincent.low");

    return account;
  }

  private List<Account> initManyAccounts() {

    List<Account> accountList = new ArrayList<>();

    Account account1 = new Account();
    account1.setFirstName("A");
    account1.setLastName("A");
    account1.setBirthDate(LocalDate.MIN);
    account1.setUsername("A.A");

    Account account2 = new Account();
    account2.setFirstName("B");
    account2.setLastName("B");
    account2.setBirthDate(LocalDate.MIN);
    account2.setUsername("B.B");

    Account account3 = new Account();
    account3.setFirstName("C");
    account3.setLastName("C");
    account3.setBirthDate(LocalDate.MIN);
    account3.setUsername("C.C");

    accountList.add(account1);
    accountList.add(account2);
    accountList.add(account3);

    return accountList;
  }

  @Test
  void createOne_success() {

    Account account = initOneAccount();
    account.setId(null);

    CreateAccountRequest request = new CreateAccountRequest();
    BeanUtils.copyProperties(account, request);

    accountService.createOne(request);

    ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);
    Mockito.verify(accountRepository)
        .save(accountArgumentCaptor.capture());

    Account result = accountArgumentCaptor.getValue();
    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(account);
  }

  @Test
  void createOne_whenUsernameIsExists_shouldReturnStatusConflictAndNeverSaved() {

    Account account = initOneAccount();

    CreateAccountRequest request = new CreateAccountRequest();
    BeanUtils.copyProperties(account, request);

    BDDMockito.given(accountHelper.isPresent(anyString()))
        .willReturn(true);

    Assertions.assertThat(accountService.createOne(request)
        .getStatus())
        .isEqualTo(STATUS_CONFLICT());

    Mockito.verify(accountRepository, never())
        .save(any());
  }

  @Test
  void createOne_whenRequestIsNull_shouldReturnStatusNoContentNeverSaved() {

    Assertions.assertThat(accountService.createOne(null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRepository, never())
        .save(any());
  }

  @Test
  void createMany_success() {

    List<Account> accountList = initManyAccounts();
    List<CreateAccountRequest> requestList = new ArrayList<>();

    for (int i = 0; i < accountList.size(); i++) {
      CreateAccountRequest request = new CreateAccountRequest();
      BeanUtils.copyProperties(accountList.get(i), request);
      requestList.add(request);
    }

    accountService.createMany(requestList);

    ArgumentCaptor<List<Account>> listAccountCaptor = ArgumentCaptor.forClass(List.class);
    Mockito.verify(accountRepository)
        .saveAll(listAccountCaptor.capture());

    List<Account> capturedAccounts = listAccountCaptor.getValue();

    for (int i = 0; i < requestList.size(); i++) {
      Assertions.assertThat(capturedAccounts.get(i))
          .usingRecursiveComparison()
          .isEqualTo(accountList.get(i));
    }
  }

  @Test
  void createMany_whenUsernameIsExists_shouldReturnStatusConflictAndNeverSaved() {

    List<Account> accountList = initManyAccounts();
    List<CreateAccountRequest> requestList = new ArrayList<>();

    for (int i = 0; i < accountList.size(); i++) {
      CreateAccountRequest request = new CreateAccountRequest();
      BeanUtils.copyProperties(accountList.get(i), request);
      requestList.add(request);
    }

    BDDMockito.given(accountHelper.isPresent(anyString()))
        .willReturn(true);

    Assertions.assertThat(accountService.createMany(requestList)
        .getStatus())
        .isEqualTo(STATUS_CONFLICT());

    Mockito.verify(accountRepository, never())
        .saveAll(any());
  }

  @Test
  void createMany_whenRequestIsEmptyList_shouldReturnNoContentAndNeverSaved() {

    Assertions.assertThat(accountService.createMany(new ArrayList<>())
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRepository, never())
        .saveAll(any());
  }

  @Test
  void createMany_whenRequestIsNull_shouldReturnStatusNoContentAndNeverSaved() {

    Assertions.assertThat(accountService.createMany(null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRepository, never())
        .saveAll(any());
  }

  @Test
  void findAll_success() {

    accountService.findAll();

    Mockito.verify(accountRepository)
        .findAll();
  }

  @Test
  void findById_success() {

    Account account = initOneAccount();

    accountService.findById(account.getId());

    ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(accountHelper)
        .findOneAccount(studentIdArgumentCaptor.capture());

    Long resultId = studentIdArgumentCaptor.getValue();
    Assertions.assertThat(resultId)
        .isEqualTo(account.getId());
  }

  @Test
  void findById_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeverFound() {

    Account account = initOneAccount();

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(accountService.findById(account.getId())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(accountRepository, never())
        .findById(any());
  }

  @Test
  void findById_whenRequestIsNull_shouldReturnStatusNoContentAndNeverFound() {

    Assertions.assertThat(accountService.findById(null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRepository, never())
        .findById(any());
  }

  @Test
  void updateOne_whenAccountExistsAndUsernameIsUnique_success() {

    Account existedAccount = initOneAccount();

    Account updatedAccount = initOneAccount();
    updatedAccount.setUsername("jamet");

    UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
    BeanUtils.copyProperties(updatedAccount, updateAccountRequest);

    BDDMockito.given(accountHelper.findOneAccount(existedAccount.getId()))
        .willReturn(existedAccount);
    BDDMockito.given(accountHelper.isPresent(updatedAccount.getUsername()))
        .willReturn(false);

    accountService.updateOne(existedAccount.getId(), updateAccountRequest);

    Mockito.verify(accountRepository)
        .save(any());
  }

  @Test
  void updateOne_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeverSaved() {

    Account updateAccount = initOneAccount();

    UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
    BeanUtils.copyProperties(updateAccount, updateAccountRequest);

    BDDMockito.given(accountHelper.findOneAccount(updateAccount.getId()))
        .willReturn(null);

    Assertions.assertThat(accountService.updateOne(updateAccount.getId(), updateAccountRequest)
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(accountRepository, never())
        .save(updateAccount);
  }

  @Test
  void updateOne_whenUsernameIsTaken_shouldReturnStatusConflictAndNeverSaved() {

    Account updateAccount = initOneAccount();

    UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
    BeanUtils.copyProperties(updateAccount, updateAccountRequest);

    BDDMockito.given(accountHelper.findOneAccount(updateAccount.getId()))
        .willReturn(updateAccount);
    BDDMockito.given(accountHelper.isPresent(updateAccountRequest.getUsername()))
        .willReturn(true);
    BDDMockito.given(accountHelper.isUsernameEquals(updateAccountRequest.getUsername(), updateAccount.getUsername()))
        .willReturn(false);

    Assertions.assertThat(accountService.updateOne(updateAccount.getId(), updateAccountRequest)
        .getStatus())
        .isEqualTo(STATUS_CONFLICT());

    Mockito.verify(accountRepository, never())
        .save(updateAccount);
  }

  @Test
  void deleteById_success() {

    Account account = initOneAccount();

    BDDMockito.given(accountHelper.findOneAccount(account.getId()))
        .willReturn(account);

    accountService.deleteById(account.getId());

    ArgumentCaptor<Long> accountIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(accountRepository)
        .deleteById(accountIdArgumentCaptor.capture());

    Long capturedStudentId = accountIdArgumentCaptor.getValue();
    Assertions.assertThat(capturedStudentId)
        .isEqualTo(account.getId());
  }

  @Test
  void deleteById_whenAccountDoesNotExists_shouldReturnStatusNotFoundAndNeverDeleted() {

    Account account = initOneAccount();

    BDDMockito.given(accountHelper.findOneAccount(anyLong()))
        .willReturn(null);

    Assertions.assertThat(accountService.deleteById(account.getId())
        .getStatus())
        .isEqualTo(STATUS_NOT_FOUND());

    Mockito.verify(accountRepository, never())
        .deleteById(account.getId());
  }

  @Test
  void deleteById_whenRequestIsNull_shouldReturnStatusNoContentAndNeverDeleted() {

    Assertions.assertThat(accountService.deleteById(null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRepository, never())
        .deleteById(any());
  }

  @Test
  void follow_success() {

    Account follower = initOneAccount();

    Account followed = initOneAccount();
    followed.setId(2L);
    followed.setFirstName("jamet");

    AccountRelationship accountRelationship = new AccountRelationship();
    accountRelationship.setFollower(follower);
    accountRelationship.setFollowed(followed);

    CreateAccountRelationshipRequest request = new CreateAccountRelationshipRequest();
    request.setFollowerId(accountRelationship.getFollower()
        .getId());
    request.setFollowedId(accountRelationship.getFollowed()
        .getId());
    BeanUtils.copyProperties(follower, request);
    BeanUtils.copyProperties(followed, request);

    BDDMockito.given(accountHelper.findOneAccount(follower.getId()))
        .willReturn(accountRelationship.getFollower());
    BDDMockito.given(accountHelper.findOneAccount(followed.getId()))
        .willReturn(accountRelationship.getFollowed());

    accountService.follow(request);

    ArgumentCaptor<AccountRelationship> accountRelationshipArgumentCaptor =
        ArgumentCaptor.forClass(AccountRelationship.class);
    Mockito.verify(accountRelationshipRepository)
        .save(accountRelationshipArgumentCaptor.capture());

    AccountRelationship result = accountRelationshipArgumentCaptor.getValue();
    Assertions.assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(accountRelationship);
  }

  @Test
  void follow_whenFollowerAccountDoesNotExist_shouldReturnStatusUnprocessableAndNeverSaved() {

    Account follower = initOneAccount();

    Account followed = initOneAccount();
    followed.setId(2L);
    followed.setFirstName("jamet");

    AccountRelationship accountRelationship = new AccountRelationship();
    accountRelationship.setFollower(follower);
    accountRelationship.setFollowed(followed);

    CreateAccountRelationshipRequest request = new CreateAccountRelationshipRequest();
    request.setFollowerId(accountRelationship.getFollower()
        .getId());
    request.setFollowedId(accountRelationship.getFollowed()
        .getId());
    BeanUtils.copyProperties(follower, request);
    BeanUtils.copyProperties(followed, request);

    BDDMockito.given(accountHelper.findOneAccount(follower.getId()))
        .willReturn(null);

    Assertions.assertThat(accountService.follow(request)
        .getStatus())
        .isEqualTo(STATUS_UNPROCESSABLE());

    Mockito.verify(accountRelationshipRepository, never())
        .save(accountRelationship);
  }

  @Test
  void follow_whenFollowedAccountDoesNotExist_shouldReturnStatusUnprocessableAndNeverSaved() {

    Account follower = initOneAccount();

    Account followed = initOneAccount();
    followed.setId(2L);
    followed.setFirstName("jamet");

    AccountRelationship accountRelationship = new AccountRelationship();
    accountRelationship.setFollower(follower);
    accountRelationship.setFollowed(followed);

    CreateAccountRelationshipRequest request = new CreateAccountRelationshipRequest();
    request.setFollowerId(accountRelationship.getFollower()
        .getId());
    request.setFollowedId(accountRelationship.getFollowed()
        .getId());
    BeanUtils.copyProperties(follower, request);
    BeanUtils.copyProperties(followed, request);

    BDDMockito.given(accountHelper.findOneAccount(follower.getId()))
        .willReturn(accountRelationship.getFollower());
    BDDMockito.given(accountHelper.findOneAccount(followed.getId()))
        .willReturn(null);

    Assertions.assertThat(accountService.follow(request)
        .getStatus())
        .isEqualTo(STATUS_UNPROCESSABLE());

    Mockito.verify(accountRelationshipRepository, never())
        .save(accountRelationship);
  }

  @Test
  void follow_whenRequestIsNull_shouldReturnStatusNoContentAndNeverSaved() {

    Assertions.assertThat(accountService.follow(null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRelationshipRepository, never())
        .save(any());
  }

  @Test
  void unfollow_success() {

    Account follower = initOneAccount();

    Account followed = initOneAccount();
    followed.setId(2L);
    followed.setFirstName("jamet");

    AccountRelationship accountRelationship = new AccountRelationship();
    accountRelationship.setFollower(follower);
    accountRelationship.setFollowed(followed);

    CreateAccountRelationshipRequest request = new CreateAccountRelationshipRequest();
    request.setFollowedId(accountRelationship.getFollowed()
        .getId());
    request.setFollowerId(accountRelationship.getFollower()
        .getId());

    BDDMockito.given(accountHelper.findOneRelationship(request))
        .willReturn(accountRelationship);

    accountService.unfollow(request);

    ArgumentCaptor<AccountRelationship> accountRelationshipArgumentCaptor =
        ArgumentCaptor.forClass(AccountRelationship.class);
    Mockito.verify(accountRelationshipRepository)
        .delete(accountRelationshipArgumentCaptor.capture());

    AccountRelationship capturedAccountRelationship = accountRelationshipArgumentCaptor.getValue();
    Assertions.assertThat(capturedAccountRelationship)
        .isEqualTo(accountRelationship);
  }

  @Test
  void unfollow_whenRelationDoesNotExists_shouldReturnStatusUnprocessableAndNeverDeleted() {

    Account follower = initOneAccount();

    Account followed = initOneAccount();
    followed.setId(2L);
    followed.setFirstName("jamet");

    AccountRelationship accountRelationship = new AccountRelationship();
    accountRelationship.setFollower(follower);
    accountRelationship.setFollowed(followed);

    CreateAccountRelationshipRequest request = new CreateAccountRelationshipRequest();
    request.setFollowedId(accountRelationship.getFollowed()
        .getId());
    request.setFollowerId(accountRelationship.getFollower()
        .getId());

    BDDMockito.given(accountHelper.findOneRelationship(request))
        .willReturn(null);

    Assertions.assertThat(accountService.unfollow(request)
        .getStatus())
        .isEqualTo(STATUS_UNPROCESSABLE());

    Mockito.verify(accountRelationshipRepository, never())
        .save(accountRelationship);
  }

  @Test
  void unfollow_whenRequestIsNull_shouldReturnStatusNoContentAndNeverDeleted() {

    Assertions.assertThat(accountService.unfollow(null)
        .getStatus())
        .isEqualTo(STATUS_NO_CONTENT());

    Mockito.verify(accountRelationshipRepository, never())
        .delete(any());
  }
}
