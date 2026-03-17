package com.sublethub.service;

import com.sublethub.dto.WxLoginRequest;
import com.sublethub.entity.User;
import com.sublethub.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 微信登录：实际生产需调用微信接口用 code 换 openId，此处模拟
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxAuthService {

  private final UserMapper userMapper;

  @Transactional
  public User loginOrRegister(WxLoginRequest dto) {
    String openId = dto.getCode();
    if (openId == null || openId.isEmpty()) {
      openId = "mock_openid_" + System.currentTimeMillis();
    }
    User user = userMapper.selectByOpenId(openId);
    if (user != null) {
      user.setNickName(dto.getNickName() != null ? dto.getNickName() : user.getNickName());
      user.setAvatarUrl(dto.getAvatarUrl() != null ? dto.getAvatarUrl() : user.getAvatarUrl());
      user.setUpdateTime(LocalDateTime.now());
      userMapper.updateById(user);
      return user;
    }
    user = new User();
    user.setOpenId(openId);
    user.setNickName(dto.getNickName() != null ? dto.getNickName() : "微信用户");
    user.setAvatarUrl(dto.getAvatarUrl());
    user.setCreateTime(LocalDateTime.now());
    user.setUpdateTime(LocalDateTime.now());
    userMapper.insert(user);
    return user;
  }
}
