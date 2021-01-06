package com.patrick.sneakerkilleradmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.patrick.sneakerkilleradmin.mapper")
public class SneakerKillerAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SneakerKillerAdminApplication.class, args);
    }

}
