package com.vincent.mutualan.mutualankuy.service;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.role.CreateRoleRequest;
import com.vincent.mutualan.mutualankuy.model.role.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

  BaseResponse<List<RoleResponse>> findAll();

  BaseResponse<List<RoleResponse>> createManyRoles(List<CreateRoleRequest> requests);
}
