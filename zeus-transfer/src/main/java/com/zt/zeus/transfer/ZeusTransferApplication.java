package com.zt.zeus.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
public class ZeusTransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeusTransferApplication.class, args);
    }

}
