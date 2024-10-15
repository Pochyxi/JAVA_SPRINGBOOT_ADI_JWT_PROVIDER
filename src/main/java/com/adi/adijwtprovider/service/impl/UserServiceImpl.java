package com.adi.adijwtprovider.service.impl;

import com.adi.adijwtprovider.dto.ProfilePermissionDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;
import com.adi.adijwtprovider.service.ApiService;
import com.adi.adijwtprovider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ApiService apiService;

    @Override
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

    @Override
    public boolean existsByUsernameOrEmail( String usernameOrEmail ) {
        return Boolean.TRUE.equals( apiService.existsByUsernameOrEmail( usernameOrEmail ).block() );
    }

    @Override
    public Set<ProfilePermissionDTO> getProfilePermissions( Long profileId ) {
        return apiService.getProfilePermissions( profileId ).block();
    }
}
