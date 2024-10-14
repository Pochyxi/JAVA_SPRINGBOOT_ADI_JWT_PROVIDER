package com.adi.adijwtprovider.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    private static final String BASE_URL = "http://localhost:8082/api/user";

    private final SecurityProperties securityProperties;

    public WebClientConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public WebClient webClient() {
        // Usa l'utente di lettura per l'autenticazione (modifica secondo le tue esigenze)
        String username = securityProperties.getWrite().getUsername();
        String password = securityProperties.getWrite().getPassword();

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}

