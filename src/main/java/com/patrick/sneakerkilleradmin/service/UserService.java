package com.patrick.sneakerkilleradmin.service;

import com.patrick.sneakerkilleradmin.entity.Menu;
import com.patrick.sneakerkilleradmin.entity.Role;
import com.patrick.sneakerkilleradmin.entity.User;
import com.patrick.sneakerkilleradmin.mapper.MenuMapper;
import com.patrick.sneakerkilleradmin.mapper.PermissionMapper;
import com.patrick.sneakerkilleradmin.mapper.RoleMapper;
import com.patrick.sneakerkilleradmin.mapper.UserMapper;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserMapper userMapper;
    PermissionMapper permissionMapper;
    MenuMapper menuMapper;
    RoleMapper roleMapper;

    @Autowired
    public UserService(UserMapper userMapper, PermissionMapper permissionMapper, MenuMapper menuMapper, RoleMapper roleMapper){
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.menuMapper = menuMapper;
        this.roleMapper = roleMapper;
    }

    public User findByName(String username){
        return userMapper.getByUsername(username);
    }

    public boolean addUser(User user){
        // html转义
        String username = HtmlUtils.htmlEscape(user.getUsername());
        String password = user.getPassword();
        user.setUsername(username);
        User userInDb = userMapper.getByUsername(username);
        //用户名存在
        if(userInDb !=null){
            return false;
        }else {
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            // 设置 hash 算法迭代次数
            int times = 2;
            // 得到 hash 后的密码
            String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
            // 存储用户信息，包括 salt 与 hash 后的密码
            user.setSalt(salt);
            user.setPassword(encodedPassword);
            userMapper.save(user);
            int uuid = user.getId();
            //默认给一个普通用户角色
            roleMapper.insertDefaultRole(uuid);
            return true;
        }
    }

    public List<Menu> listAllMenus(String username){
        User user = userMapper.getUserWithRole(username);
        List<Integer> rids = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        return menuMapper.getByRoleIds(rids);
    }

    public List<User> listAllUsers(){
        return userMapper.getAllUserWithRole();
    }

    public List<Role> getAllRoleInDb(){
        return roleMapper.listAll();
    }

    public void modifyUserRole(User user){
        //先删除所有权限
        Integer uid = user.getId();
        userMapper.deleteCurUserRole(uid);
        //再依此插入新权限
        List<Integer> rids = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        rids.forEach(rid ->{
            userMapper.insertCurUserRole(uid,rid);
        });
    }

    public void resetPassword(User user){
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码
        String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userMapper.updatePassword(user);
    }

    public boolean modifyPassword(Map<String,String> map){
        String username = map.get("username");
        String origin = map.get("originPassword");
        String reset = map.get("resetPassword");
        User user = userMapper.getByUsername(username);
        String encodedPassword = new SimpleHash("md5", origin, user.getSalt(), 2).toString();
        if(encodedPassword.equals(user.getPassword())){
            String salt = new SecureRandomNumberGenerator().nextBytes().toString();
            String encoded = new SimpleHash("md5", reset, salt, 2).toString();
            user.setSalt(salt);
            user.setPassword(encoded);
            userMapper.updatePassword(user);
            return true;
        }else {
            //原始密码不对
            return false;
        }
    }
}
