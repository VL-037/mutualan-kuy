package com.vincent.mutualan.mutualankuy.model.accountRelationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRelationshipRequest {

  @NotNull
  private Long followerId;

  @NotNull
  private Long followedId;
}
