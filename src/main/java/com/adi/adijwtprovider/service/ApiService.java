package com.adi.adijwtprovider.service;

import com.adi.adijwtprovider.dto.ProfilePermissionDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApiService {

    private final WebClient webClient;

    public ApiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<UserDTOInternal> getUserData( String usernameOrEmail) {
        return webClient.get()
                .uri("/username_email/{usernameOrEmail}", usernameOrEmail)
                .retrieve()
                .bodyToMono( UserDTOInternal.class);
    }

    public Mono<Boolean> existsByUsernameOrEmail(String usernameOrEmail) {
        return webClient.get()
                .uri("/username_email/exist/{usernameOrEmail}", usernameOrEmail)
                .retrieve()
                .bodyToMono( Boolean.class);
    }

    public Mono<Set<ProfilePermissionDTO>> getProfilePermissions( Long profileId) {
        return webClient.get()
                .uri("/profile_permissions/{profileId}", profileId)
                .retrieve()
                .bodyToFlux( ProfilePermissionDTO.class)
                .collect( Collectors.toSet());
    }
}
