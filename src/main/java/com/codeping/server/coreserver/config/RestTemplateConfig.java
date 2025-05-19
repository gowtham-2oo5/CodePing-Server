package com.codeping.server.coreserver.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Configuration
public class RestTemplateConfig {

    @Bean
    RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(10));
        factory.setReadTimeout(Duration.ofSeconds(40));
        return new RestTemplate(factory);
    }

    @Bean
    Bucket rateLimiter() {
        // Create a rate limiter that allows 100 requests per minute
        Bandwidth limit = Bandwidth.simple(100, Duration.ofMinutes(1));

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}