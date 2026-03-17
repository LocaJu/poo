package com.sublethub.service;

import com.sublethub.entity.AdminUser;
import com.sublethub.exception.BusinessException;
import com.sublethub.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

  private final AdminUserMapper adminUserMapper;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AdminUser login(String username, String password) {
    AdminUser admin = adminUserMapper.selectByUsername(username);
    if (admin == null) {
      throw new BusinessException(401, "用户名或密码错误");
    }
    if (!passwordEncoder.matches(password, admin.getPassword())) {
      throw new BusinessException(401, "用户名或密码错误");
    }
    return admin;
  }
}
