package com.adi.adijwtprovider.service;


import com.adi.adijwtprovider.dto.JwtAuthResponseDTO;
import com.adi.adijwtprovider.dto.LoginDTO;
import com.adi.adijwtprovider.dto.SignupDTO;

public interface AuthenticationService {

    JwtAuthResponseDTO login( String username, String password );

    Void createUser( SignupDTO signupDTO, boolean confEmail);
}
