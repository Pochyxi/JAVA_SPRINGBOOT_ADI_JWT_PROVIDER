package com.adi.adijwtprovider.controller;


import com.adi.adijwtprovider.dto.JwtAuthResponseDTO;
import com.adi.adijwtprovider.dto.LoginDTO;
import com.adi.adijwtprovider.dto.UserDTO;
import com.adi.adijwtprovider.dto.UserDTOInternal;
import com.adi.adijwtprovider.exception.ResourceNotFoundException;
import com.adi.adijwtprovider.security.JwtTokenProvider;
import com.adi.adijwtprovider.service.AuthenticationService;
import com.adi.adijwtprovider.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger( AuthenticationController.class);


    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    /**
     * LOGIN
     * Questo metodo permette di effettuare il login
     * PREAUTHORIZATION: NONE
     * @param loginDTO DTO contenente username e password
     */
    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthResponseDTO> login( @RequestBody LoginDTO loginDTO ) {

        JwtAuthResponseDTO jwtAuthResponseDTO = authenticationService.login( loginDTO.getUsernameOrEmail(), loginDTO.getPassword() );


        return new ResponseEntity<>( jwtAuthResponseDTO, HttpStatus.OK );
    }


    /**
     * Endpoint per il refresh del token di autenticazione.
     *
     * @param request HttpServletRequest contenente il token attuale
     * @return ResponseEntity con il nuovo token JWT
     */
    @GetMapping(value = "/refresh")
    @SneakyThrows
    public ResponseEntity<JwtAuthResponseDTO> refreshAndGetAuthenticationToken( HttpServletRequest request)
    {
        logger.info("Tentativo Refresh Token");

        String tokenHeader = "Authorization";

        String authToken = request.getHeader( tokenHeader );

        // Controllo se il token inizia con "Bearer " e rimuovo i primi 7 caratteri
        if (authToken != null && authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7); // Rimuovi i primi 7 caratteri ("Bearer ")
        }

        if (authToken == null)
        {
            throw new Exception("Token assente o non valido!");
        }

        if (jwtTokenProvider.canTokenBeRefreshed( authToken ))
        {
            String refreshedToken = jwtTokenProvider.refreshToken( authToken );

            String username = jwtTokenProvider.getUsernameFromToken( refreshedToken );

            Optional<UserDTOInternal> userDTOInternal = userService.findByUsernameOrEmail( username);

            if( userDTOInternal.isEmpty() )
            {
                throw new ResourceNotFoundException("Utente non trovato: " + username);
            }

            UserDTO userDTO = authenticationService.mapUserDtoInternalToUserDto( userDTOInternal.get() );

            JwtAuthResponseDTO jwtAuthResponseDTO = new JwtAuthResponseDTO();
            jwtAuthResponseDTO.setAccessToken( refreshedToken );
            jwtAuthResponseDTO.setUser( userDTO );

            logger.warn( "Refreshed Token {}", refreshedToken );

            return ResponseEntity.ok(jwtAuthResponseDTO);
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
