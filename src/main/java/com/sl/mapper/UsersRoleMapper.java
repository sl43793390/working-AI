package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.UsersRole;
import com.sl.entity.UsersRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersRoleMapper  extends BaseMapper<UsersRole>{
    int deleteByExample(UsersRoleExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("roleId") String roleId);

    int insert(UsersRole record);

    int insertSelective(UsersRole record);

    List<UsersRole> selectByExample(UsersRoleExample example);

    UsersRole selectByPrimaryKey(@Param("userId") String userId, @Param("roleId") String roleId);

    int updateByExampleSelective(@Param("record") UsersRole record, @Param("example") UsersRoleExample example);

    int updateByExample(@Param("record") UsersRole record, @Param("example") UsersRoleExample example);

    int updateByPrimaryKeySelective(UsersRole record);

    int updateByPrimaryKey(UsersRole record);

    int insertBatch(List<UsersRole> records);
}