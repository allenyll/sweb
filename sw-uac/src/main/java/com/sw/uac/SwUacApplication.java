package com.sw.uac;

import com.sw.client.annotion.EnableLoginArgResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan("com.sw")
@EnableDiscoveryClient
@EnableLoginArgResolver
@EnableFeignClients("com.sw")
@SpringBootApplication
public class SwUacApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwUacApplication.class, args);
    }

}
