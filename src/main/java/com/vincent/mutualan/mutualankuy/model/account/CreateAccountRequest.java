package com.vincent.mutualan.mutualankuy.model.account;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateAccountRequest {

  @NotBlank
  private String firstName;

  private String middleName = "";

  @NotBlank
  private String lastName;

  @NotBlank
  private LocalDate birthDate;

  @NotBlank
  private String username = "";

  private String bio = "";

  // private String email;
  //
  // private String password;
}
