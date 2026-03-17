package com.sublethub.mapper;

import com.sublethub.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

  User selectById(Long id);

  User selectByOpenId(@Param("openId") String openId);

  List<User> selectPage(@Param("offset") int offset, @Param("limit") int limit);

  long count();

  int insert(User user);

  int updateById(User user);
}
