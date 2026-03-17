package com.sublethub.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class WxLoginRequest {

  @NotBlank(message = "code 不能为空")
  private String code;
  private String nickName;
  private String avatarUrl;
}
