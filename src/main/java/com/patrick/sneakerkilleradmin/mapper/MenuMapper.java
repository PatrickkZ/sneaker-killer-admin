package com.patrick.sneakerkilleradmin.mapper;

import com.patrick.sneakerkilleradmin.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper {
    List<Menu> getByRoleIds(List<Integer> roleIds);
}
