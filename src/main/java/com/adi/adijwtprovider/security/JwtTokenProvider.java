package com.adi.adijwtprovider.security;

import com.adi.adijwtprovider.service.impl.UserServiceImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger( JwtTokenProvider.class );

    UserServiceImpl userService;

    private final JwtConfig jwtConfig;


    private final Clock clock = DefaultClock.INSTANCE;

    @Autowired
    public JwtTokenProvider( UserServiceImpl userService, JwtConfig jwtConfig ) {
        this.userService = userService;
        this.jwtConfig = jwtConfig;
    }

    /**
     * GENERATE TOKEN
     * Questo metodo genera il token.
     * Il token contiene l'username, la data di creazione, la data di scadenza e i permessi dell'utente.
     *
     * @param authentication Oggetto Authentication
     * @return Token JWT generato
     */
    public String generateToken( Authentication authentication ) {

        String username = authentication.getName();

        Date currentDate = new Date();
        Date expirationDate = new Date( currentDate.getTime() + jwtConfig.getJwtExpirationMilliseconds() );


        return Jwts.builder()
                .setSubject( username )
                .setIssuedAt( currentDate )
                .setExpiration( expirationDate )
                .signWith( key() )
                .compact();
    }

    /**
     * Rinnova un token JWT esistente.
     *
     * @param token Token JWT da rinnovare
     * @return Nuovo token JWT rinnovato
     */
    public String refreshToken( String token ) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate( createdDate );

        final Claims claims = getAllClaimsFromToken( token );
        claims.setIssuedAt( createdDate );
        claims.setExpiration( expirationDate );

        return Jwts.builder()
                .setClaims( claims )
                .signWith( key() )
                .compact();
    }


    /**
     * Verifica se il token può essere rinnovato.
     *
     * @param token Token JWT
     * @return true se il token può essere rinnovato, false altrimenti
     */
    public Boolean canTokenBeRefreshed( String token ) {
        return (isTokenExpired( token ));
    }


    /**
     * Verifica se il token è scaduto.
     *
     * @param token Token JWT
     * @return true se il token è ancora valido, false altrimenti
     */
    private Boolean isTokenExpired( String token ) {
        final Date expiration = getExpirationDateFromToken( token );

        boolean retVal = expiration != null;

        if( retVal ) {
            logger.info( "Token Ancora Valido!" );
        } else {
            logger.warn( "Token Scaduto o non Valido!" );
        }

        return retVal;
    }


    /**
     * Estrae tutti i claims dal token JWT.
     *
     * @param token Token JWT
     * @return Oggetto Claims contenente tutti i claims del token
     */
    private Claims getAllClaimsFromToken( String token ) {
        Claims retVal = null;

        try {

            retVal = Jwts.parserBuilder()
                    .setSigningKey( key() )
                    .setAllowedClockSkewSeconds( 60 )
                    .build()
                    .parseClaimsJws( token )
                    .getBody();
        } catch( Exception ex ) {
            logger.warn( "Errore nel parsing del token JWT: {}", ex.getMessage() );
        }

        return retVal;
    }


    /**
     * Ottiene la data di scadenza del token.
     *
     * @param token Token JWT
     * @return Data di scadenza del token
     */
    public Date getExpirationDateFromToken( String token ) {
        return getClaimFromToken( token, Claims::getExpiration );
    }


    /**
     * Estrae un claim specifico dal token JWT.
     *
     * @param token          Token JWT
     * @param claimsResolver Funzione per estrarre il claim desiderato
     * @return Valore del claim estratto
     */
    public <T> T getClaimFromToken( String token, Function<Claims, T> claimsResolver ) {
        final Claims claims = getAllClaimsFromToken( token );

        if( claims != null ) {
            logger.info( "Emissione Token:  {}", claims.getIssuedAt().toString() );
            logger.info( "Scadenza Token:  {}", claims.getExpiration().toString() );

            return claimsResolver.apply( claims );
        } else
            return null;
    }


    /**
     * Calcola la data di scadenza del token.
     *
     * @param createdDate Data di creazione del token
     * @return Data di scadenza del token
     */
    private Date calculateExpirationDate( Date createdDate ) {
        return new Date( createdDate.getTime() + jwtConfig.getJwtExpirationMilliseconds() * 1000L );
    }


    /**
     * KEY
     * Questo metodo recupera la chiave segreta dal file properties.
     * La chiave viene convertita in un oggetto Key.
     * @return Oggetto Key
     */
    private Key key() {

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode( jwtConfig.getJwtSecret() )
        );
    }

    /**
     * Estrae il nome utente o l'email dal token JWT.
     *
     * @param token Token JWT
     * @return Nome utente o email estratto dal token
     */
    public String getUsernameFromToken( String token ) {
        try {

            // Estrae i claims dal token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey( key() )
                    .build()
                    .parseClaimsJws( token )
                    .getBody();

            // Restituisce il subject, che di solito contiene l'username o l'email
            return claims.getSubject();
        } catch( Exception ex ) {
            // In caso di errore, logga l'eccezione o gestiscila come preferisci
            System.out.println( "Errore nell'estrazione del nome utente dal token: " + ex.getMessage() );
            return null;
        }
    }

}
