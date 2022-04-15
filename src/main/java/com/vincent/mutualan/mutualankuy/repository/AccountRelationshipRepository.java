package com.vincent.mutualan.mutualankuy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vincent.mutualan.mutualankuy.entity.AccountRelationship;

@Repository
public interface AccountRelationshipRepository extends JpaRepository<AccountRelationship, Long> {

  @Query(
      value = "SELECT ar FROM AccountRelationship ar WHERE ar.follower.id=(SELECT id FROM Account WHERE id=?1) AND ar.followed.id=(SELECT id FROM Account WHERE id=?2)")
  Optional<AccountRelationship> findOneAccountRelationship(Long followerId, Long followedId);

}
