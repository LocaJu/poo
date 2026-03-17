package com.sublethub.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 房源表 houses */
@Data
public class House {

  private Long id;
  private Long userId;
  private String title;
  private String type;
  private BigDecimal area;
  private BigDecimal price;
  private String deposit;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate availableDate;
  private String description;
  private String images;
  private HouseLocation location;
  /** pending / approved / rejected / offline */
  private String status = "pending";
  private String rejectReason;
  private Integer viewCount = 0;
  private Integer favoriteCount = 0;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
