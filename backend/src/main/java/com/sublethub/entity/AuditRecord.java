package com.sublethub.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 审核记录表 audits */
@Data
public class AuditRecord {

  private Long id;
  private String targetType;
  private Long targetId;
  private Long auditorId;
  private String result;
  private String reason;
  private LocalDateTime createTime;
}
