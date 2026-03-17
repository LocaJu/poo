package com.sublethub.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一 API 返回格式：{ code: 0, data: any, message: string }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

  private int code;
  private T data;
  private String message;

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(0, data, "success");
  }

  public static <T> ApiResponse<T> ok() {
    return ok(null);
  }

  public static <T> ApiResponse<T> fail(int code, String message) {
    return new ApiResponse<>(code, null, message);
  }

  public static <T> ApiResponse<T> fail(String message) {
    return fail(-1, message);
  }
}
