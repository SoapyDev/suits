package com.backend.suits;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.Optional;

@SpringBootApplication
@PropertySource("classpath:cors.properties")
public class SuitsApplication {

    @Value("${cors.allowedOrigins}")
    private String allowedOriginsConfig;

    public static void main(String[] args) {
        SpringApplication.run(SuitsApplication.class, args);
    }

    @Bean("allowedOrigins")
    public String[] getAllowedOrigins() {
        return Optional.ofNullable(System.getenv("ALLOWED_ORIGINS"))
                .orElse(this.allowedOriginsConfig).split(",");
    }

}
