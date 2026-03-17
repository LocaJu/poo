package com.sublethub.controller;

import com.sublethub.common.ApiResponse;
import com.sublethub.config.JwtUtil;
import com.sublethub.dto.WxLoginRequest;
import com.sublethub.entity.AdminUser;
import com.sublethub.entity.User;
import com.sublethub.service.AdminAuthService;
import com.sublethub.service.WxAuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证：微信登录、管理后台登录
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final WxAuthService wxAuthService;
  private final AdminAuthService adminAuthService;
  private final JwtUtil jwtUtil;

  /**
   * 小程序微信登录
   */
  @PostMapping("/wx-login")
  public ApiResponse<Map<String, Object>> wxLogin(@Valid @RequestBody WxLoginRequest dto) {
    User user = wxAuthService.loginOrRegister(dto);
    String token = jwtUtil.generateToken(user.getId(), user.getOpenId());
    Map<String, Object> data = new HashMap<>();
    data.put("token", token);
    data.put("userId", user.getId());
    data.put("user", user);
    return ApiResponse.ok(data);
  }

  /**
   * 管理后台登录
   */
  @PostMapping("/admin-login")
  public ApiResponse<Map<String, Object>> adminLogin(@Valid @RequestBody AdminLoginRequest dto) {
    AdminUser admin = adminAuthService.login(dto.getUsername(), dto.getPassword());
    String token = jwtUtil.generateAdminToken(admin.getId());
    Map<String, Object> data = new HashMap<>();
    data.put("token", token);
    data.put("adminId", admin.getId());
    data.put("username", admin.getUsername());
    return ApiResponse.ok(data);
  }

  @Data
  public static class AdminLoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
  }
}
