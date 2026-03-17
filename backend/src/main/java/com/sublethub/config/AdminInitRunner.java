package com.sublethub.config;

import com.sublethub.entity.AdminUser;
import com.sublethub.mapper.AdminUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 首次启动时创建默认管理员 admin / admin123
 */
@Component
@RequiredArgsConstructor
public class AdminInitRunner implements CommandLineRunner {

  private final AdminUserMapper adminUserMapper;

  @Override
  public void run(String... args) {
    if (adminUserMapper.selectByUsername("admin") == null) {
      AdminUser admin = new AdminUser();
      admin.setUsername("admin");
      admin.setPassword(new BCryptPasswordEncoder().encode("admin123"));
      admin.setName("管理员");
      adminUserMapper.insert(admin);
    }
  }
}
