package com.vincent.mutualan.mutualankuy.model.accountRelationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRelationshipResponse {

  private static final long serialVersionUID = 6659584132588460516L;

  private Long id;

  private Long followerId;

  private Long followedId;

  private Date createdAt;

  private Date updatedAt;

  @Override
  public String toString() {

    return "AccountRelationshipResponse{" +
        "id=" + id +
        ", followerId=" + followerId +
        ", followedId=" + followedId +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
