package com.sublethub.mapper;

import com.sublethub.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {

  List<Message> selectConversation(@Param("userId1") Long userId1, @Param("userId2") Long userId2,
                                   @Param("offset") int offset, @Param("limit") int limit);

  long countUnreadByToUserId(Long toUserId);

  int insert(Message message);
}
