package com.adi.adijwtprovider.service;


import com.adi.adijwtprovider.dto.JwtAuthResponseDTO;

public interface AuthenticationService {

    JwtAuthResponseDTO login( String username, String password );
}
