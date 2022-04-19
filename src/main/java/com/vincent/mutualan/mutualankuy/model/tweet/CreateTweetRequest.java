package com.vincent.mutualan.mutualankuy.model.tweet;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTweetRequest {

  @NotBlank
  private String message;

  private Date createdAt;

  private Date updatedAt;
}
