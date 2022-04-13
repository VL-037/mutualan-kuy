package com.vincent.mutualan.mutualankuy.service.impl;

import com.vincent.mutualan.mutualankuy.entity.Role;
import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.role.CreateRoleRequest;
import com.vincent.mutualan.mutualankuy.model.role.RoleResponse;
import com.vincent.mutualan.mutualankuy.repository.RoleRepository;
import com.vincent.mutualan.mutualankuy.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public BaseResponse<List<RoleResponse>> findAll() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = roles.stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());

        return BaseResponse.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(roleResponses)
                .build();
    }

    @Override
    public BaseResponse<List<RoleResponse>> createManyRoles(List<CreateRoleRequest> requests) {
        List<RoleResponse> roleResponses = requests.stream()
                .map(this::saveOneRole)
                .collect(Collectors.toList())
                .stream()
                .map(this::toRoleResponse)
                .collect(Collectors.toList());

        return BaseResponse.<List<RoleResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(roleResponses)
                .build();
    }

    private Role saveOneRole(CreateRoleRequest request) {
        Role role = new Role();
        BeanUtils.copyProperties(request, role);

        return roleRepository.save(role);
    }

    private RoleResponse toRoleResponse(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        BeanUtils.copyProperties(role, roleResponse);
        return roleResponse;
    }
}
