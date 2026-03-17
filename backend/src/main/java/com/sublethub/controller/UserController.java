package com.sublethub.controller;

import com.sublethub.common.ApiResponse;
import com.sublethub.entity.User;
import com.sublethub.interceptor.AuthInterceptor;
import com.sublethub.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/me")
  public ApiResponse<User> me(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    User user = userService.getById(userId);
    return ApiResponse.ok(user);
  }

  @PutMapping("/me")
  public ApiResponse<User> updateMe(HttpServletRequest request, @RequestBody ProfileUpdateDto dto) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    User user = userService.updateProfile(userId, dto.getPhone(), dto.getWechatId());
    return ApiResponse.ok(user);
  }

  @PostMapping("/me/verify")
  public ApiResponse<User> submitVerify(HttpServletRequest request, @RequestBody VerifyDto dto) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    User user = userService.submitVerify(userId, dto.getRealName(), dto.getIdCardNumber(),
        dto.getIdCardFront(), dto.getIdCardBack());
    return ApiResponse.ok(user);
  }

  @GetMapping("/{id}")
  public ApiResponse<User> getById(@PathVariable Long id) {
    User user = userService.getById(id);
    return ApiResponse.ok(user);
  }

  @Data
  public static class ProfileUpdateDto {
    private String phone;
    private String wechatId;
  }

  @Data
  public static class VerifyDto {
    private String realName;
    private String idCardNumber;
    private String idCardFront;
    private String idCardBack;
  }
}
