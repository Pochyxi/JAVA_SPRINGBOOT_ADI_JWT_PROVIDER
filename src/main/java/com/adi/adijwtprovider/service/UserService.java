package com.adi.adijwtprovider.service;

import com.adi.adijwtprovider.dto.ProfilePermissionDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final ApiService apiService;

    public Optional<UserDTOInternal> findByUsernameOrEmail( String usernameOrEmail) {
        return apiService.getUserData(usernameOrEmail)
                .onErrorResume(ex -> {
                    // Gestisci l'errore per le risposte 404 o altri errori
                    if (ex instanceof WebClientResponseException.NotFound) {
                        return Mono.empty();
                    }
                    return Mono.error(ex);
                })
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .block();
    }

    public boolean existsByUsernameOrEmail( String usernameOrEmail ) {
        return Boolean.TRUE.equals( apiService.existsByUsernameOrEmail( usernameOrEmail ).block() );
    }


    public Set<ProfilePermissionDTO> getProfilePermissions( Long profileId ) {
        return apiService.getProfilePermissions( profileId ).block();
    }
}
