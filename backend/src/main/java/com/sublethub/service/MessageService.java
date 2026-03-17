package com.sublethub.service;

import com.sublethub.entity.Message;
import com.sublethub.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

  private final MessageMapper messageMapper;

  public List<Message> getConversation(Long userId1, Long userId2, int page, int pageSize) {
    int offset = (page - 1) * pageSize;
    return messageMapper.selectConversation(userId1, userId2, offset, pageSize);
  }

  public long getUnreadCount(Long toUserId) {
    return messageMapper.countUnreadByToUserId(toUserId);
  }
}
