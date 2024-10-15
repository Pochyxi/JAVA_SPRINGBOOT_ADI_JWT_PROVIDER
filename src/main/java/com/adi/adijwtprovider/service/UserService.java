package com.adi.adijwtprovider.service;

import com.adi.adijwtprovider.dto.ProfilePermissionDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    Optional<UserDTOInternal> findByUsernameOrEmail( String usernameOrEmail);
    boolean existsByUsernameOrEmail( String usernameOrEmail );
    Set<ProfilePermissionDTO> getProfilePermissions( Long profileId );
}
