package com.zt.zeus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ZeusEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeusEurekaApplication.class, args);
    }

}
