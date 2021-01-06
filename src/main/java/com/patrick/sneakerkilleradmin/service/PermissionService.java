package com.patrick.sneakerkilleradmin.service;

import com.patrick.sneakerkilleradmin.entity.Permission;
import com.patrick.sneakerkilleradmin.entity.Role;
import com.patrick.sneakerkilleradmin.entity.User;
import com.patrick.sneakerkilleradmin.mapper.PermissionMapper;
import com.patrick.sneakerkilleradmin.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    UserMapper userMapper;
    PermissionMapper permissionMapper;

    @Autowired
    public PermissionService(UserMapper userMapper, PermissionMapper permissionMapper){
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
    }

    public List<Permission> listAllPermissions(String username){
        // 用户名查角色
        User user = userMapper.getUserWithRole(username);
        List<Integer> rids = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        return permissionMapper.getByRoleIds(rids);
    }

    public boolean needFilter(String requestAPI){
        List<Permission> permissions = permissionMapper.findAll();
        for (Permission p:permissions){
            if(requestAPI.startsWith(p.getUrl())){
                return true;
            }
        }
        return false;
    }
}
