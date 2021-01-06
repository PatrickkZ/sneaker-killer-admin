package com.patrick.sneakerkilleradmin.mapper;

import com.patrick.sneakerkilleradmin.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    Permission getById(Integer id);
    List<Permission> getByRoleId(Integer roleId);
    List<Permission> getByRoleIds(List<Integer> roleIds);
    List<Permission> findAll();
}
