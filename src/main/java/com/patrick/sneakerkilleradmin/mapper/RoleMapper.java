package com.patrick.sneakerkilleradmin.mapper;

import com.patrick.sneakerkilleradmin.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    Role getById(Integer id);
    Role getRoleWithPermission(Integer id);
    List<Role> listAll();
    List<Role> getByUserId(Integer userId);
    void insertDefaultRole(Integer uid);
}
