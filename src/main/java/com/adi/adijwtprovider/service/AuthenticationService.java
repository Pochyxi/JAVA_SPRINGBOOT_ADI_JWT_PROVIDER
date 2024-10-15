package com.adi.adijwtprovider.service;


import com.adi.adijwtprovider.dto.JwtAuthResponseDTO;
import com.adi.adijwtprovider.dto.UserDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;

public interface AuthenticationService {

    JwtAuthResponseDTO login( String username, String password );

    UserDTO mapUserDtoInternalToUserDto( UserDTOInternal user);
}
