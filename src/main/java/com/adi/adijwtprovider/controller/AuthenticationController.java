package com.adi.adijwtprovider.controller;


import com.adi.adijwtprovider.dto.JwtAuthResponseDTO;
import com.adi.adijwtprovider.dto.LoginDTO;
import com.adi.adijwtprovider.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController( AuthenticationService authenticationService ) {
        this.authenticationService = authenticationService;
    }

    /**
     * LOGIN
     * Questo metodo permette di effettuare il login
     * PREAUTHORIZATION: NONE
     */
    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthResponseDTO> login( @RequestBody LoginDTO loginDTO ) {

        JwtAuthResponseDTO jwtAuthResponseDTO = authenticationService.login( loginDTO.getUsernameOrEmail(), loginDTO.getPassword() );


        return new ResponseEntity<>( jwtAuthResponseDTO, HttpStatus.OK );
    }
}
