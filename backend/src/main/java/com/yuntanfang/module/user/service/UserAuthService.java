package com.yuntanfang.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntanfang.common.BusinessException;
import com.yuntanfang.module.user.entity.Admin;
import com.yuntanfang.module.user.entity.Role;
import com.yuntanfang.module.user.entity.User;
import com.yuntanfang.module.user.entity.UserRole;
import com.yuntanfang.module.user.mapper.AdminMapper;
import com.yuntanfang.module.user.mapper.RoleMapper;
import com.yuntanfang.module.user.mapper.UserMapper;
import com.yuntanfang.module.user.mapper.UserRoleMapper;
import com.yuntanfang.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, Object> login(String username, String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user != null) {
            if (!password.equals(user.getPasswordHash())) {
                throw new BusinessException("用户名或密码错误");
            }
            String role = user.getAccountType();
            return buildLoginResult(user.getId(), user.getUsername(), role);
        }
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin != null) {
            if (!password.equals(admin.getPasswordHash())) {
                throw new BusinessException("用户名或密码错误");
            }
            return buildLoginResult(admin.getId(), admin.getUsername(), admin.getRoleCode());
        }
        throw new BusinessException("账号不存在");
    }

    private Map<String, Object> buildLoginResult(Long id, String username, String role) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", jwtTokenProvider.createToken(String.valueOf(id), role));
        result.put("tokenType", "Bearer");
        result.put("userId", id);
        result.put("username", username);
        result.put("role", role);
        return result;
    }

    @Transactional
    public Map<String, Object> register(String username, String password) {
        Long exists = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (exists != null && exists > 0) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password);
        user.setAccountType("consumer");
        user.setStatus("active");
        userMapper.insert(user);

        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleCode, "consumer"));
        if (role != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(role.getId());
            userRoleMapper.insert(userRole);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("userId", user.getId());
        result.put("username", username);
        result.put("role", "consumer");
        return result;
    }

    public Map<String, Object> me(Long userId) {
        if (userId == null) {
            throw new BusinessException("未登录或登录态已失效");
        }
        User user = userMapper.selectById(userId);
        if (user != null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", user.getId());
            result.put("username", user.getUsername());
            result.put("accountType", user.getAccountType());
            result.put("mobile", user.getMobile());
            result.put("roles", new String[]{user.getAccountType()});
            return result;
        }
        Admin admin = adminMapper.selectById(userId);
        if (admin != null) {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", admin.getId());
            result.put("username", admin.getUsername());
            result.put("accountType", admin.getRoleCode());
            result.put("roles", new String[]{admin.getRoleCode()});
            return result;
        }
        throw new BusinessException("用户不存在");
    }
}
