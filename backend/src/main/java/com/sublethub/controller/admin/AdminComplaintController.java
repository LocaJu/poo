package com.sublethub.controller.admin;

import com.sublethub.common.ApiResponse;
import com.sublethub.common.PageResult;
import com.sublethub.entity.Complaint;
import com.sublethub.entity.House;
import com.sublethub.mapper.ComplaintMapper;
import com.sublethub.mapper.HouseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/complaints")
@RequiredArgsConstructor
public class AdminComplaintController {

  private final ComplaintMapper complaintMapper;
  private final HouseMapper houseMapper;

  @GetMapping
  public ApiResponse<PageResult<Complaint>> list(@RequestParam(required = false) String status,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
    int offset = (page - 1) * pageSize;
    List<Complaint> list;
    long total;
    if (status != null && !status.isEmpty()) {
      list = complaintMapper.selectByStatus(status, offset, pageSize);
      total = complaintMapper.countByStatus(status);
    } else {
      list = complaintMapper.selectPage(offset, pageSize);
      total = complaintMapper.count();
    }
    return ApiResponse.ok(PageResult.of(list != null ? list : Collections.emptyList(), total, page, pageSize));
  }

  @PostMapping("/{id}/resolve")
  public ApiResponse<Void> resolve(@PathVariable Long id, @RequestBody Map<String, String> body) {
    Complaint c = complaintMapper.selectById(id);
    if (c != null) {
      c.setStatus("resolved");
      c.setResult(body.get("result"));
      complaintMapper.updateById(c);
      if ("house".equals(c.getTargetType()) && "下架".equals(body.get("action"))) {
        House h = houseMapper.selectById(c.getTargetId());
        if (h != null) {
          h.setStatus("offline");
          houseMapper.updateById(h);
        }
      }
    }
    return ApiResponse.ok();
  }
}
