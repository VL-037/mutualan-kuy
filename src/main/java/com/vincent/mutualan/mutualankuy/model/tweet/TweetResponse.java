package com.vincent.mutualan.mutualankuy.model.tweet;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetResponse implements Serializable {

  private static final long serialVersionUID = 6659584132588460517L;

  private Long id;

  private Long creatorId;

  private String message;

  private Date createdAt;

  private Date updatedAt;

  @Override
  public String toString() {

    return "TweetResponse{" +
        "id=" + id +
        ", creatorId=" + creatorId +
        ", message='" + message + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
