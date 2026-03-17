package com.sublethub.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 管理员账号 */
@Data
public class AdminUser {

  private Long id;
  private String username;
  private String password;
  private String name;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
