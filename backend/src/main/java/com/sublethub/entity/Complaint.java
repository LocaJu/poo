package com.sublethub.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 举报表 complaints */
@Data
public class Complaint {

  private Long id;
  private Long complainantId;
  private String targetType;
  private Long targetId;
  private String reason;
  private String description;
  private String status = "pending";
  private String result;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
