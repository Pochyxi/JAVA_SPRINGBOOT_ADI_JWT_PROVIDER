package com.adi.adijwtprovider.service;

import com.adi.adijwtprovider.dto.ProfilePermissionDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ApiService {
    Mono<UserDTOInternal> getUserData( String usernameOrEmail);
    Mono<Boolean> existsByUsernameOrEmail(String usernameOrEmail);
    Mono<Set<ProfilePermissionDTO>> getProfilePermissions( Long profileId);
}
