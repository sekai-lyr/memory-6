package com.sekai.game2048;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sekai.game2048.mapper")
public class Sekai2048Application {

    public static void main(String[] args) {
        SpringApplication.run(Sekai2048Application.class, args);
    }
}
