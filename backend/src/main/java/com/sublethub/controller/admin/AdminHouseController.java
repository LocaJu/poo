package com.sublethub.controller.admin;

import com.sublethub.common.ApiResponse;
import com.sublethub.common.PageResult;
import com.sublethub.entity.House;
import com.sublethub.mapper.HouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理后台 - 房源审核与管理
 */
@RestController
@RequestMapping("/admin/houses")
@RequiredArgsConstructor
public class AdminHouseController {

  private final HouseMapper houseMapper;

  @GetMapping("/pending")
  public ApiResponse<PageResult<House>> pending(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
    int offset = (page - 1) * pageSize;
    var list = houseMapper.selectByStatus("pending", offset, pageSize);
    long total = houseMapper.countByStatus("pending");
    return ApiResponse.ok(PageResult.of(list, total, page, pageSize));
  }

  @PostMapping("/{id}/approve")
  public ApiResponse<Void> approve(@PathVariable Long id) {
    House house = houseMapper.selectById(id);
    if (house != null) {
      house.setStatus("approved");
      houseMapper.updateById(house);
    }
    return ApiResponse.ok();
  }

  @PostMapping("/{id}/reject")
  public ApiResponse<Void> reject(@PathVariable Long id, @RequestBody Map<String, String> body) {
    House house = houseMapper.selectById(id);
    if (house != null) {
      house.setStatus("rejected");
      house.setRejectReason(body.get("reason"));
      houseMapper.updateById(house);
    }
    return ApiResponse.ok();
  }

  @GetMapping
  public ApiResponse<PageResult<House>> list(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int pageSize) {
    int offset = (page - 1) * pageSize;
    var list = houseMapper.selectPage(offset, pageSize);
    long total = houseMapper.countAll();
    return ApiResponse.ok(PageResult.of(list != null ? list : java.util.Collections.emptyList(), total, page, pageSize));
  }
}
