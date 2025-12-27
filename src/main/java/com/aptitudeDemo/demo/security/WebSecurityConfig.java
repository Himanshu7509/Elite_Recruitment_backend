package com.aptitudeDemo.demo.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    



    // Main security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Enable CORS (frontend support)
            .cors(cors ->  cors.configurationSource(corsConfigurationSource()))

            // Disable CSRF for stateless REST APIs
            .csrf(csrf -> csrf.disable())

            // Stateless session (JWT / API based)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/auth/**",
                    "/open/generate",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/feedback/**",
                    "/result/**",
                    "/email-verification/**"
                ).permitAll()

                // All other endpoints require authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }

    // CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Update these for production
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:5173",
                "https://elite-apptitude-test-fswdxsy4b-purvanshu-khapres-projects.vercel.app",
                "https://elite-apptitude-test.vercel.app",
                "https://unperpetuating-may-eely.ngrok-free.dev/"

        ));

        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
