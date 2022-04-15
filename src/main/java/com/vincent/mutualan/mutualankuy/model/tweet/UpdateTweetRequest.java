package com.vincent.mutualan.mutualankuy.model.tweet;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTweetRequest {

  @NotBlank
  private String message;
}
