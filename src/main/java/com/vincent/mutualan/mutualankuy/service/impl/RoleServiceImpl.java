package com.vincent.mutualan.mutualankuy.service.impl;

import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.STATUS_OK;
import static com.vincent.mutualan.mutualankuy.helper.response.ResponseHelper.getBaseResponse;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vincent.mutualan.mutualankuy.entity.Role;
import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.role.CreateRoleRequest;
import com.vincent.mutualan.mutualankuy.model.role.RoleResponse;
import com.vincent.mutualan.mutualankuy.repository.RoleRepository;
import com.vincent.mutualan.mutualankuy.service.RoleService;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public BaseResponse<List<RoleResponse>> findAll() {

    List<Role> roles = roleRepository.findAll();
    List<RoleResponse> roleResponses = roles.stream()
        .map(this::toRoleResponse)
        .collect(Collectors.toList());

    return getBaseResponse(roleResponses, STATUS_OK());
  }

  @Override
  public BaseResponse<List<RoleResponse>> createManyRoles(List<CreateRoleRequest> requests) {

    List<RoleResponse> roleResponses = requests.stream()
        .map(this::saveOneRole)
        .collect(Collectors.toList())
        .stream()
        .map(this::toRoleResponse)
        .collect(Collectors.toList());

    return getBaseResponse(roleResponses, STATUS_OK());
  }

  private RoleResponse toRoleResponse(Role role) {

    RoleResponse roleResponse = new RoleResponse();
    BeanUtils.copyProperties(role, roleResponse);
    return roleResponse;
  }

  private Role saveOneRole(CreateRoleRequest request) {

    Role role = new Role();
    BeanUtils.copyProperties(request, role);

    return roleRepository.save(role);
  }
}
