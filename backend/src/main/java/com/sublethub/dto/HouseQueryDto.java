package com.sublethub.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class HouseQueryDto {

  private String keyword;
  private List<String> districts;
  private BigDecimal priceMin;
  private BigDecimal priceMax;
  private String type;
  private LocalDate startDate;
  private LocalDate endDate;
  /** default / latest / price_asc / price_desc / area_desc */
  private String orderBy = "default";
  private int page = 1;
  private int pageSize = 10;
}
