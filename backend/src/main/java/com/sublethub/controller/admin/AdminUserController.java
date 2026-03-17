package com.sublethub.controller.admin;

import com.sublethub.common.ApiResponse;
import com.sublethub.common.PageResult;
import com.sublethub.entity.User;
import com.sublethub.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

  private final UserMapper userMapper;

  @GetMapping
  public ApiResponse<PageResult<User>> list(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int pageSize) {
    int offset = (page - 1) * pageSize;
    var list = userMapper.selectPage(offset, pageSize);
    long total = userMapper.count();
    return ApiResponse.ok(PageResult.of(list != null ? list : Collections.emptyList(), total, page, pageSize));
  }

  @PostMapping("/{id}/status")
  public ApiResponse<Void> setStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
    User user = userMapper.selectById(id);
    if (user != null) {
      user.setStatus(body.getOrDefault("status", 0));
      userMapper.updateById(user);
    }
    return ApiResponse.ok();
  }

  @PostMapping("/{id}/verify")
  public ApiResponse<Void> setVerified(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
    User user = userMapper.selectById(id);
    if (user != null) {
      user.setIsVerified(body.getOrDefault("verified", false));
      userMapper.updateById(user);
    }
    return ApiResponse.ok();
  }
}
