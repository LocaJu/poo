package com.sublethub.mapper;

import com.sublethub.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminUserMapper {

  AdminUser selectById(Long id);

  AdminUser selectByUsername(@Param("username") String username);

  int insert(AdminUser admin);

  int updateById(AdminUser admin);
}
