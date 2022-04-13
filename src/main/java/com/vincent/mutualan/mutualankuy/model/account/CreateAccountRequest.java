package com.vincent.mutualan.mutualankuy.model.account;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAccountRequest {

  private String firstName;

  private String middleName = "";

  private String lastName = "";

  private LocalDate birthDate;

  private String username = "";

  private String bio = "";

  // private String email;
  //
  // private String password;
}
