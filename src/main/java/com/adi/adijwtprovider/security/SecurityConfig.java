package com.adi.adijwtprovider.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    private final AuthEntryPoint authEntryPoint;


    public SecurityConfig( AuthEntryPoint authEntryPoint ) {
        this.authEntryPoint = authEntryPoint;
    }


    /* PASSWORD ENCODER
         * Questo metodo crea un oggetto PasswordEncoder.
         * Utilizza BCryptPasswordEncoder, che implementa l'algoritmo BCrypt per la codifica delle password.
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    /* AUTHENTICATION MANAGER
         * Questo metodo crea un oggetto AuthenticationManager.
         * Prende come input un oggetto AuthenticationConfiguration.
         * Utilizza la configurazione di autenticazione di Spring Security per ottenere l'AuthenticationManager.
         * L'AuthenticationManager è un componente chiave di Spring Security che gestisce il processo di autenticazione.
         * Restituisce l'oggetto AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration configuration ) throws Exception {

        return configuration.getAuthenticationManager();
    }


    /* SECURITY FILTER CHAIN
         * Questo metodo configura la sicurezza dell'applicazione.
         * Configura la politica CORS, disabilita CSRF, configura le regole di autorizzazione delle richieste, gestisce le eccezioni e configura la gestione della sessione.
         * Aggiunge il filtro per la gestione dell'autenticazione JWT.
     */
    @Bean
    SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
        http
                .cors( (httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer
                        .configurationSource( ( httpServletRequest ) -> {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(
                                    List.of(
                                            "http://localhost:4200/",
                                            "http://127.0.0.1:4200/"
                                    ) );
                            corsConfiguration.setAllowedMethods(
                                    List.of(
                                            "GET",
                                            "POST",
                                            "PUT",
                                            "DELETE",
                                            "OPTIONS"
                                    ) );
                            corsConfiguration.setAllowedHeaders( List.of( "*" ) );
                            corsConfiguration.setExposedHeaders( List.of( "Authorization" ) );
                            corsConfiguration.setMaxAge( 3600L );
                            return corsConfiguration;
                        } )

                ) )
                .csrf( AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( (authorize) -> authorize
                        // Permetti l'accesso a /api/auth/** e / in un unico requestMatcher
                        .requestMatchers("/api/auth/**", "/").permitAll()

                        // Blocca tutte le altre richieste (richiede autenticazione)
                        .anyRequest().denyAll()
                )
                .exceptionHandling( exception -> exception
                        // Gestione delle eccezioni per utenti non autenticati che cercano di accedere a risorse protette
                        .authenticationEntryPoint(authEntryPoint)
                )

                // GESTIONE DELLA SESSIONE STATELESS
                .sessionManagement( session -> session
                        .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                );

        return http.build();
    }


}
