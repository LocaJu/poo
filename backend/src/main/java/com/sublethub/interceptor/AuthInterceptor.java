package com.sublethub.interceptor;

import com.sublethub.config.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 鉴权拦截器：从 Header Authorization 解析 JWT，将 userId 写入 request attribute
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

  private static final String HEADER_AUTH = "Authorization";
  private static final String PREFIX_BEARER = "Bearer ";
  public static final String ATTR_USER_ID = "userId";
  public static final String ATTR_ADMIN_ID = "adminId";

  private final JwtUtil jwtUtil;

  public AuthInterceptor(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 公开接口：GET 房源列表与详情
    if ("GET".equalsIgnoreCase(request.getMethod())) {
      String path = request.getRequestURI();
      if (path != null && (path.endsWith("/houses") || path.matches(".*/houses/\\d+$"))) {
        return true;
      }
    }

    String auth = request.getHeader(HEADER_AUTH);
    if (auth == null || !auth.startsWith(PREFIX_BEARER)) {
      response.setStatus(401);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write("{\"code\":401,\"message\":\"未登录或 token 无效\"}");
      return false;
    }
    String token = auth.substring(PREFIX_BEARER.length()).trim();
    if (jwtUtil.isAdminToken(token)) {
      Long adminId = jwtUtil.getAdminIdFromToken(token);
      if (adminId == null) {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"管理员 token 无效\"}");
        return false;
      }
      request.setAttribute(ATTR_ADMIN_ID, adminId);
      return true;
    }
    Long userId = jwtUtil.getUserIdFromToken(token);
    if (userId == null) {
      response.setStatus(401);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write("{\"code\":401,\"message\":\"token 无效\"}");
      return false;
    }
    String path = request.getRequestURI();
    if (path != null && path.contains("/admin/")) {
      response.setStatus(403);
      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write("{\"code\":403,\"message\":\"需要管理员权限\"}");
      return false;
    }
    request.setAttribute(ATTR_USER_ID, userId);
    return true;
  }
}
