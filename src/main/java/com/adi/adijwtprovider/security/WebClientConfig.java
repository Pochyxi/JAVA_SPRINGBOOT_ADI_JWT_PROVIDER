package com.adi.adijwtprovider.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final SecurityProperties securityProperties;

    public WebClientConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public WebClient webClient() {

        // Usa l'utente di lettura per l'autenticazione (modifica secondo le tue esigenze)
        String username = securityProperties.getWrite().getUsername();
        String password = securityProperties.getWrite().getPassword();
        String base = securityProperties.getEndpoint().getBase();

        return WebClient.builder()
                .baseUrl(base)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }
}

