package com.sublethub.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 用户表 users */
@Data
public class User {

  private Long id;
  private String openId;
  private String nickName;
  private String avatarUrl;
  private String phone;
  private String wechatId;
  private Boolean isVerified = false;
  private String idCardFront;
  private String idCardBack;
  private String realName;
  private String idCardNumber;
  /** 0 正常 1 禁用 */
  private Integer status = 0;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
