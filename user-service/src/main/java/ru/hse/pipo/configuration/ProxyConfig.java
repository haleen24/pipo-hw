package ru.hse.pipo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ProxyConfig {

    @Value("${proxy.target-url}")
    private String targetUrl;

    @Bean
    public RestClient proxyRestClient() {
        return RestClient.builder()
            .baseUrl(targetUrl)
            .build();
    }
}