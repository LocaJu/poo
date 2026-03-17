package com.sublethub.controller.admin;

import com.sublethub.common.ApiResponse;
import com.sublethub.mapper.HouseMapper;
import com.sublethub.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

  private final UserMapper userMapper;
  private final HouseMapper houseMapper;

  @GetMapping("/stats")
  public ApiResponse<Map<String, Object>> stats() {
    long totalUsers = userMapper.count();
    long totalHouses = houseMapper.countAll();
    long pendingHouses = houseMapper.countPending();
    long todayNew = houseMapper.countTodayNewApproved();
    Map<String, Object> data = new HashMap<>();
    data.put("totalUsers", totalUsers);
    data.put("totalHouses", totalHouses);
    data.put("pendingHouses", pendingHouses);
    data.put("todayNewHouses", todayNew);
    return ApiResponse.ok(data);
  }
}
