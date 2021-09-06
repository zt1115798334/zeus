package com.zt.zeus.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
public class ZeusTransferLocalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeusTransferLocalApplication.class, args);
    }

}
