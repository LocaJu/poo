package com.sublethub.entity;

import lombok.Data;

import java.time.LocalDateTime;

/** 收藏表 favorites */
@Data
public class Favorite {

  private Long id;
  private Long userId;
  private Long houseId;
  private LocalDateTime createTime;
}
