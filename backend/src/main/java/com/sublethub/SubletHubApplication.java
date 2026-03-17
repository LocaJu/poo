package com.sublethub;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SubletHub 转租信息发布平台 - 后端启动类（MyBatis XML 配置方式）
 */
@SpringBootApplication
@MapperScan("com.sublethub.mapper")
public class SubletHubApplication {

  public static void main(String[] args) {
    SpringApplication.run(SubletHubApplication.class, args);
  }
}
