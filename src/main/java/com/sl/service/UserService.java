package com.sl.service;

import com.sl.entity.*;
import com.sl.mapper.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

	@Resource
	private UserMapper userMapper;
	@Resource
	private RoleMapper roleMapper;
	@Resource
	private UsersRoleMapper userRoleMapper;
	@Resource
	private RolesPermissionMapper rolePermissionMapper;
	@Resource
	private PermissionMapper permissionMapper;
	
	public User getUserById(String userId) {
		UserExample ex = new UserExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		List<User> selectByExample = userMapper.selectByExample(ex);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return selectByExample.get(0);
		}
		return null;
	}
	
	public UsersRole getRoleByUserId(String userId) {
		UsersRoleExample ex = new UsersRoleExample();
		ex.createCriteria().andUserIdEqualTo(userId);
		List<UsersRole> selectByExample = userRoleMapper.selectByExample(ex);
		if (null != selectByExample && !selectByExample.isEmpty()) {
			return selectByExample.get(0);
		}
		return null;
	}
	
	public List<RolesPermission> getRolePermissionByRoleId(String roleId){
		RolesPermissionExample ex = new RolesPermissionExample();
		ex.createCriteria().andRoleIdEqualTo(roleId);
		List<RolesPermission> selectByExample = rolePermissionMapper.selectByExample(ex);
		return selectByExample;
	}
	
}
