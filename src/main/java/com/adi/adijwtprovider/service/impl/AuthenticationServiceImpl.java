package com.adi.adijwtprovider.service.impl;

import com.adi.adijwtprovider.dto.*;
import com.adi.adijwtprovider.exception.ErrorCodeList;
import com.adi.adijwtprovider.exception.ResourceNotFoundException;
import com.adi.adijwtprovider.exception.appException;
import com.adi.adijwtprovider.security.JwtTokenProvider;
import com.adi.adijwtprovider.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserServiceImpl userService;

    private final JwtTokenProvider jwtTokenProvider;


    /* LOGIN
        * Questo metodo gestisce il processo di autenticazione di un utente.
     */
    @Override
    public JwtAuthResponseDTO login( String username, String password ) {

        // Controlla se l'username o l'email forniti esistono nel database.
        if (!userService.existsByUsernameOrEmail(username)) {
            throw new appException(HttpStatus.BAD_REQUEST, ErrorCodeList.BADCREDENTIALS);
        }

        // Autentica l'utente utilizzando l'oggetto AuthenticationManager.
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // Imposta l'oggetto Authentication nel SecurityContext.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Recupera l'utente dal database
        UserDTOInternal user = userService.findByUsernameOrEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCodeList.NF404
                ));

        // Crea un nuovo oggetto UserDTO e popola i suoi campi con i dati dell'utente.
        // Per evitare di mostrare la password, non viene incluso il campo password.
        UserDTO userDTO = mapUserDtoInternalToUserDto( user );

        // Crea un nuovo oggetto JwtAuthResponseDTO e popola i suoi campi con il token di accesso e l'utente.
        JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
        jwtAuthResponseDTO.setAccessToken(jwtTokenProvider.generateToken(authentication));
        jwtAuthResponseDTO.setUser( userDTO );


        // Restituisce l'oggetto JwtAuthResponseDTO.
        return jwtAuthResponseDTO;
    }

    public UserDTO mapUserDtoInternalToUserDto(UserDTOInternal user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEnabled( user.isEnabled() )
                .isTemporaryPassword( user.isTemporaryPassword() )
                .dateTokenCheck( user.getDateTokenCheck() )
                .profileName( user.getProfileName() )
                .profilePermissions( user.getProfilePermissions() )
                .build();
    }

}
