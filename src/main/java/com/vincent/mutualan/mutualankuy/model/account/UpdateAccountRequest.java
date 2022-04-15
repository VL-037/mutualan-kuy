package com.vincent.mutualan.mutualankuy.model.account;

import javax.validation.constraints.NotBlank;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAccountRequest {

  @NotBlank
  private String firstName;

  @Nullable
  private String middleName;

  @NotBlank
  private String lastName;

  @NotBlank
  private String username;

  @Nullable
  private String bio;

  // private String email;
  //
  // private String password;
  //
}
