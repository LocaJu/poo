package com.sublethub.entity;

import lombok.Data;

/** 房源地理位置（嵌入 House） */
@Data
public class HouseLocation {

  private String province;
  private String city;
  private String district;
  private String address;
  private Double latitude;
  private Double longitude;
}
