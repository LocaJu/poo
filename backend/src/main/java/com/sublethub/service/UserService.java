package com.sublethub.service;

import com.sublethub.entity.User;
import com.sublethub.exception.BusinessException;
import com.sublethub.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;

  public User getById(Long id) {
    User user = userMapper.selectById(id);
    if (user == null) {
      throw new BusinessException(404, "用户不存在");
    }
    return user;
  }

  @Transactional
  public User updateProfile(Long userId, String phone, String wechatId) {
    User user = getById(userId);
    if (phone != null) user.setPhone(phone);
    if (wechatId != null) user.setWechatId(wechatId);
    userMapper.updateById(user);
    return user;
  }

  @Transactional
  public User submitVerify(Long userId, String realName, String idCardNumber, String idCardFront, String idCardBack) {
    User user = getById(userId);
    user.setRealName(realName);
    user.setIdCardNumber(idCardNumber);
    user.setIdCardFront(idCardFront);
    user.setIdCardBack(idCardBack);
    user.setIsVerified(false);
    userMapper.updateById(user);
    return user;
  }
}
