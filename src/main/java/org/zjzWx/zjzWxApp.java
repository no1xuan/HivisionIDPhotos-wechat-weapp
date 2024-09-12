package org.zjzWx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class zjzWxApp {
    public static void main(String[] args) {
        SpringApplication.run(zjzWxApp.class, args);
    }
}
