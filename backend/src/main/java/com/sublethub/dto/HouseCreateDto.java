package com.sublethub.dto;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class HouseCreateDto {

  @NotBlank
  @Size(max = 20)
  private String title;

  @Size(max = 20)
  private String type;

  @DecimalMin("0")
  private BigDecimal area;

  @NotNull
  @DecimalMin("0")
  private BigDecimal price;

  @Size(max = 32)
  private String deposit;

  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate availableDate;

  @Size(max = 500)
  private String description;

  @Size(max = 9)
  private List<String> images;

  @Valid
  private LocationDto location;

  @Data
  public static class LocationDto {
    private String province;
    private String city;
    private String district;
    private String address;
    private Double latitude;
    private Double longitude;
  }
}
