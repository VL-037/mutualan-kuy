package com.vincent.mutualan.mutualankuy.controller;

import com.vincent.mutualan.mutualankuy.model.BaseResponse;
import com.vincent.mutualan.mutualankuy.model.role.CreateRoleRequest;
import com.vincent.mutualan.mutualankuy.model.role.RoleResponse;
import com.vincent.mutualan.mutualankuy.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/roles")
public class RoleController {

  @Autowired
  private RoleService roleService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<RoleResponse>> getRoles() {

    return roleService.findAll();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public BaseResponse<List<RoleResponse>> saveManyRoles(@RequestBody List<CreateRoleRequest> requests) {

    return roleService.createManyRoles(requests);
  }
}
