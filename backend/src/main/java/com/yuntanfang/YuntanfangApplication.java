package com.yuntanfang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yuntanfang.module.**.mapper")
@SpringBootApplication
public class YuntanfangApplication {

    public static void main(String[] args) {
        SpringApplication.run(YuntanfangApplication.class, args);
    }
}
