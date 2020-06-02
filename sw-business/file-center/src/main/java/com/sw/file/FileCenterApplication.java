package com.sw.file;

import com.sw.client.annotion.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableLoginArgResolver
@EnableFeignClients("com.sw")
@ComponentScan("com.sw")
@EnableDiscoveryClient
@EnableSwagger2
@SpringBootApplication
public class FileCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileCenterApplication.class, args);
    }

}
