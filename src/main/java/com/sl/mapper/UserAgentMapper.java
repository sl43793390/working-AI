package com.sl.mapper;

import com.sl.entity.UserAgent;
import com.sl.entity.UserAgentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAgentMapper {
    int deleteByExample(UserAgentExample example);

    int deleteByPrimaryKey(@Param("userId") String userId, @Param("idAgent") String idAgent);

    int insert(UserAgent record);

    int insertSelective(UserAgent record);

    List<UserAgent> selectByExample(UserAgentExample example);

    UserAgent selectByPrimaryKey(@Param("userId") String userId, @Param("idAgent") String idAgent);

    int updateByPrimaryKeySelective(UserAgent record);

    int updateByPrimaryKey(UserAgent record);
}