package com.sublethub.controller;

import com.sublethub.common.ApiResponse;
import com.sublethub.entity.Complaint;
import com.sublethub.interceptor.AuthInterceptor;
import com.sublethub.service.ComplaintService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/complaints")
@RequiredArgsConstructor
public class ComplaintController {

  private final ComplaintService complaintService;

  @PostMapping
  public ApiResponse<Complaint> create(HttpServletRequest request, @Valid @RequestBody CreateComplaintDto dto) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    Complaint c = new Complaint();
    c.setComplainantId(userId);
    c.setTargetType(dto.getTargetType());
    c.setTargetId(dto.getTargetId());
    c.setReason(dto.getReason());
    c.setDescription(dto.getDescription());
    c = complaintService.create(c);
    return ApiResponse.ok(c);
  }

  @Data
  public static class CreateComplaintDto {
    @NotBlank
    private String targetType;
    @NotNull
    private Long targetId;
    private String reason;
    private String description;
  }
}
