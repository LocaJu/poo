package com.sublethub.controller;

import com.sublethub.common.ApiResponse;
import com.sublethub.entity.Message;
import com.sublethub.interceptor.AuthInterceptor;
import com.sublethub.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @GetMapping("/conversation/{otherUserId}")
  public ApiResponse<List<Message>> conversation(HttpServletRequest request,
                                                  @PathVariable Long otherUserId,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "20") int pageSize) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    List<Message> list = messageService.getConversation(userId, otherUserId, page, pageSize);
    return ApiResponse.ok(list);
  }

  @GetMapping("/unread-count")
  public ApiResponse<Map<String, Long>> unreadCount(HttpServletRequest request) {
    Long userId = (Long) request.getAttribute(AuthInterceptor.ATTR_USER_ID);
    long count = messageService.getUnreadCount(userId);
    return ApiResponse.ok(Map.of("count", count));
  }
}
