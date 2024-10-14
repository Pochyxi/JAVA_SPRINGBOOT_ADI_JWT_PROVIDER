package com.adi.adijwtprovider.security;

import com.adi.adijwtprovider.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    UserService userService;

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationInMs;

    @Autowired
    public JwtTokenProvider( UserService userService ) {
        this.userService = userService;
    }

    /* GENERATE TOKEN
         * Questo metodo genera il token.
         * Il token contiene l'username, la data di creazione, la data di scadenza e i permessi dell'utente.
     */
    public String generateToken( Authentication authentication ) {

        String username = authentication.getName();

        Date currentDate = new Date();
        Date expirationDate = new Date( currentDate.getTime() + jwtExpirationInMs );



        return Jwts.builder()
                .setSubject( username )
                .setIssuedAt( currentDate )
                .setExpiration( expirationDate )
                .signWith( key() )
                .compact();
    }


    /* KEY
         * Questo metodo recupera la chiave segreta dal file properties.
         * La chiave viene convertita in un oggetto Key.
     */
    private Key key() {

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode( jwtSecret )
        );
    }

}
