package org.lesley.descovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DescoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DescoveryApplication.class, args);
    }

}
