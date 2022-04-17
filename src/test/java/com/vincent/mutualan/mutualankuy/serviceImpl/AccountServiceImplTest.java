package com.vincent.mutualan.mutualankuy.serviceImpl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_CONFLICT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
import com.vincent.mutualan.mutualankuy.helper.account.AccountHelper;
import com.vincent.mutualan.mutualankuy.model.account.CreateAccountRequest;
import com.vincent.mutualan.mutualankuy.repository.AccountRelationshipRepository;
import com.vincent.mutualan.mutualankuy.repository.AccountRepository;
import com.vincent.mutualan.mutualankuy.service.AccountService;
import com.vincent.mutualan.mutualankuy.service.impl.AccountServiceImpl;

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

  @Test
  void createOne_success() {

    Account account = new Account();
    account.setFirstName("vincent");
    account.setLastName("low");
    account.setBirthDate(LocalDate.MIN);
    account.setUsername("vincent.low");

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

    Account account = new Account();
    account.setFirstName("vincent");
    account.setLastName("low");
    account.setBirthDate(LocalDate.MIN);
    account.setUsername("vincent.low");

    CreateAccountRequest request = new CreateAccountRequest();
    BeanUtils.copyProperties(account, request);

    BDDMockito.given(accountHelper.isPresent(anyString()))
        .willReturn(true);

    Assertions.assertThat(accountService.createOne(request)
        .getStatus())
        .isEqualTo(STATUS_CONFLICT());

    verify(accountRepository, never()).save(any());
  }

  @Test
  void createMany_success() {

    List<Account> accountList = new ArrayList<>();
    List<CreateAccountRequest> requestList = new ArrayList<>();

    Account account1 = new Account();
    account1.setFirstName("A");
    account1.setLastName("A");
    account1.setBirthDate(LocalDate.MIN);
    account1.setUsername("A.A");

    accountList.add(account1);
    CreateAccountRequest request1 = new CreateAccountRequest();
    BeanUtils.copyProperties(account1, request1);
    requestList.add(request1);

    Account account2 = new Account();
    account2.setFirstName("B");
    account2.setLastName("B");
    account2.setBirthDate(LocalDate.MIN);
    account2.setUsername("B.B");

    accountList.add(account2);
    CreateAccountRequest request2 = new CreateAccountRequest();
    BeanUtils.copyProperties(account2, request2);
    requestList.add(request2);

    Account account3 = new Account();
    account3.setFirstName("C");
    account3.setLastName("C");
    account3.setBirthDate(LocalDate.MIN);
    account3.setUsername("C.C");

    accountList.add(account3);
    CreateAccountRequest request3 = new CreateAccountRequest();
    BeanUtils.copyProperties(account3, request3);
    requestList.add(request3);

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

    List<Account> accountList = new ArrayList<>();
    List<CreateAccountRequest> requestList = new ArrayList<>();

    Account account1 = new Account();
    account1.setFirstName("A");
    account1.setLastName("A");
    account1.setBirthDate(LocalDate.MIN);
    account1.setUsername("A.A");

    accountList.add(account1);
    CreateAccountRequest request1 = new CreateAccountRequest();
    BeanUtils.copyProperties(account1, request1);
    requestList.add(request1);

    Account account2 = new Account();
    account2.setFirstName("B");
    account2.setLastName("B");
    account2.setBirthDate(LocalDate.MIN);
    account1.setUsername("B.B");

    accountList.add(account2);
    CreateAccountRequest request2 = new CreateAccountRequest();
    BeanUtils.copyProperties(account2, request2);
    requestList.add(request2);

    Account account3 = new Account();
    account3.setFirstName("C");
    account3.setLastName("C");
    account3.setBirthDate(LocalDate.MIN);
    account3.setUsername("C.C");

    accountList.add(account3);
    CreateAccountRequest request3 = new CreateAccountRequest();
    BeanUtils.copyProperties(account3, request3);
    requestList.add(request3);

    BDDMockito.given(accountHelper.isPresent(anyString()))
        .willReturn(true);

    Assertions.assertThat(accountService.createMany(requestList)
        .getStatus())
        .isEqualTo(STATUS_CONFLICT());

    verify(accountRepository, never()).saveAll(any());
  }

  @Test
  void findAll_success() {}

  @Test
  void findById() {}

  @Test
  void updateOne() {}

  @Test
  void deleteById() {}

  @Test
  void follow() {}

  @Test
  void unfollow() {}
}
