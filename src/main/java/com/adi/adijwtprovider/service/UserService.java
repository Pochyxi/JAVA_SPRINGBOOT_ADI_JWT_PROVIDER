package com.adi.adijwtprovider.service;

import com.adi.adijwtprovider.dto.ProfilePermissionDTO;
import com.adi.adijwtprovider.dto.SignupDTO;
import com.adi.adijwtprovider.dto.UserDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;
import com.adi.adijwtprovider.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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


    public Void save( SignupDTO signupDTO ) {
        return apiService.signup( signupDTO ).onErrorResume( ex -> {
            if (ex instanceof WebClientResponseException.BadRequest) {
                throw new IllegalArgumentException("Invalid data");
            }
            return Mono.error(ex);
        }).block();
    }

}
