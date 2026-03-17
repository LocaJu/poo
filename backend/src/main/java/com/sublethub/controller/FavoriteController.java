package com.sublethub.controller;

import com.sublethub.common.ApiResponse;
import com.sublethub.common.PageResult;
import com.sublethub.entity.House;
import com.sublethub.interceptor.AuthInterceptor;
import com.sublethub.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

  private final FavoriteService favoriteService;

  @GetMapping
  public ApiResponse<PageResult<House>> list(HttpServletRequest request,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    return ApiResponse.ok(favoriteService.list(userId, page, pageSize));
  }

  @PostMapping("/{houseId}")
  public ApiResponse<Void> add(HttpServletRequest request, @PathVariable Long houseId) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    favoriteService.add(userId, houseId);
    return ApiResponse.ok();
  }

  @DeleteMapping("/{houseId}")
  public ApiResponse<Void> remove(HttpServletRequest request, @PathVariable Long houseId) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    favoriteService.remove(userId, houseId);
    return ApiResponse.ok();
  }

  @GetMapping("/check")
  public ApiResponse<Map<String, Boolean>> check(HttpServletRequest request, @RequestParam Long houseId) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    boolean favorited = favoriteService.isFavorited(userId, houseId);
    return ApiResponse.ok(Map.of("favorited", favorited));
  }
}
