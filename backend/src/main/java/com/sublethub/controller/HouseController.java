package com.sublethub.controller;

import com.sublethub.common.ApiResponse;
import com.sublethub.common.PageResult;
import com.sublethub.dto.HouseCreateDto;
import com.sublethub.dto.HouseQueryDto;
import com.sublethub.entity.House;
import com.sublethub.interceptor.AuthInterceptor;
import com.sublethub.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

  private final HouseService houseService;

  @GetMapping
  public ApiResponse<PageResult<House>> list(HouseQueryDto query) {
    return ApiResponse.ok(houseService.query(query));
  }

  @GetMapping("/{id}")
  public ApiResponse<House> getById(@PathVariable Long id) {
    return ApiResponse.ok(houseService.getById(id));
  }

  @PostMapping
  public ApiResponse<House> create(HttpServletRequest request, @Valid @RequestBody HouseCreateDto dto) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    return ApiResponse.ok(houseService.create(userId, dto));
  }

  @PutMapping("/{id}")
  public ApiResponse<House> update(HttpServletRequest request, @PathVariable Long id,
                                   @Valid @RequestBody HouseCreateDto dto) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    return ApiResponse.ok(houseService.update(userId, id, dto));
  }

  @PostMapping("/{id}/offline")
  public ApiResponse<Void> offline(HttpServletRequest request, @PathVariable Long id) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    houseService.offline(userId, id);
    return ApiResponse.ok();
  }

  @GetMapping("/my")
  public ApiResponse<PageResult<House>> myHouses(HttpServletRequest request,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    return ApiResponse.ok(houseService.myHouses(userId, page, pageSize));
  }
}
