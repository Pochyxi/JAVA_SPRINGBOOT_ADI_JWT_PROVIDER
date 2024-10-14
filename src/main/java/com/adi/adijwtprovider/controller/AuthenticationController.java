package com.adi.adijwtprovider.controller;


import com.adi.adijwtprovider.dto.JwtAuthResponseDTO;
import com.adi.adijwtprovider.dto.LoginDTO;
import com.adi.adijwtprovider.dto.SignupDTO;
import com.adi.adijwtprovider.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        System.out.println("Received LoginDTO: " + loginDTO);
        System.out.println("Username: " + loginDTO.getUsernameOrEmail());
        System.out.println("Password: " + loginDTO.getPassword());

        JwtAuthResponseDTO jwtAuthResponseDTO = authenticationService.login( loginDTO.getUsernameOrEmail(), loginDTO.getPassword() );


        return new ResponseEntity<>( jwtAuthResponseDTO, HttpStatus.OK );
    }

    /**
     * SIGNUP
     * Questo metodo permette di effettuare la registrazione
     * PREAUTHORIZATION: NONE
     * TODO: Eliminare in produzione
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupDTO signupDTO ) {
        authenticationService.createUser( signupDTO, true );

        return new ResponseEntity<>(HttpStatus.CREATED );
    }

    /**
     * CREAZIONE UTENTE
        * Questo metodo permette di Creare un utente
        * PREAUTHORIZATION:
             * Utente con permesso USER_CREATE
        *  TODO: eliminare il produzione
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<Void>adminCreateUser(@Valid @RequestBody SignupDTO signupDTO) {

        authenticationService.createUser( signupDTO, true );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
