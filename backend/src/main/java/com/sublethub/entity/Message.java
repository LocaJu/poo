package com.sublethub.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 消息表 messages */
@Data
public class Message {

  private Long id;
  private Long fromUserId;
  private Long toUserId;
  private String content;
  private String msgType = "text";
  private Long houseId;
  private Boolean isRead = false;
  private LocalDateTime createTime;
}
