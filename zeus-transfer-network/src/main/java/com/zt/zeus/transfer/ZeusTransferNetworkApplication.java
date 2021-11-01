package com.zt.zeus.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@EnableDiscoveryClient
@SpringBootApplication
public class ZeusTransferNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeusTransferNetworkApplication.class, args);
    }

}
