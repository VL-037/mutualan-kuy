package com.vincent.mutualan.mutualankuy.repository;

import com.vincent.mutualan.mutualankuy.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
