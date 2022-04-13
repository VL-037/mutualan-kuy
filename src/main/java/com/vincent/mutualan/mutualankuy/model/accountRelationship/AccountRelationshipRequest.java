package com.vincent.mutualan.mutualankuy.model.accountRelationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRelationshipRequest {

    private Long followerId;

    private Long followedId;
}
