package com.codeping.server.coreserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;
import java.time.Duration;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.support.DefaultClientCodecConfigurer;

@Configuration
public class WebClientConfig {

    @Bean
    public ClientCodecConfigurer clientCodecConfigurer() {
        DefaultClientCodecConfigurer configurer = new DefaultClientCodecConfigurer();
        configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024); // 16MB buffer size
        return configurer;
    }

    @Bean("leetCodeWebClient")
    public WebClient leetCodeWebClient(ClientCodecConfigurer clientCodecConfigurer) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10))
                .keepAlive(true);

        return WebClient.builder()
                .baseUrl("https://leetcode.com/graphql")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean("codeChefWebClient")
    public WebClient codeChefWebClient(ClientCodecConfigurer clientCodecConfigurer) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10))
                .keepAlive(true);

        return WebClient.builder()
                .baseUrl("http://localhost:8800")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    @Bean("codeforcesWebClient")
    public WebClient codeforcesWebClient(ClientCodecConfigurer clientCodecConfigurer) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(10))
                .keepAlive(true);

        return WebClient.builder()
                .baseUrl("https://codeforces.com/api")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
}