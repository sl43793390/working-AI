package com.sl.config;

import com.sl.entity.Permission;
import com.sl.entity.User;
import com.sl.service.UserManageMentProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     *
     * Spring Security 的核心是检查 GrantedAuthority 字符串。
     * hasRole('ADMIN'): 这个表达式会检查用户是否拥有 ROLE_ADMIN 这个 GrantedAuthority。它会自动添加 ROLE_ 前缀进行匹配。
     * hasAuthority('READ_DATA'): 这个表达式会检查用户是否拥有 READ_DATA 这个确切的 GrantedAuthority 字符串。
     *      * // 假设从数据库查到角色是 "ADMIN"
     *      * List<GrantedAuthority> authorities = new ArrayList<>();
     *      * authorities.add(new SimpleGrantedAuthority("ROLE_" + "ADMIN")); // 注意这里加了 ROLE_ 前缀
     *      * return new User(username, password, authorities);
     */
    @Autowired
    private UserManageMentProcess userRepository; // 假设你有一个用于访问用户的Repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找用户信息
        User user = userRepository.selectUserById(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // 根据用户信息查找权限信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> strings = userRepository.selectRoleIdsByUserId(username);
        for (String roleId : strings){
            List<Permission> permissionIds = userRepository.selectPermissionByRoleId(roleId);
            for (Permission permission : permissionIds){
                authorities.add(new SimpleGrantedAuthority(permission.getResourceName()));
            }
        }
        // 返回包含权限信息的UserDetails对象
        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getPassword(),
                authorities);
    }
}