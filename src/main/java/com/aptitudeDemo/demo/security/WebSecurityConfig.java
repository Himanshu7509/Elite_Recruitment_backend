package com.aptitudeDemo.demo.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors ->  cors.configurationSource(corsConfigurationSource()))

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/auth/**",
                    "/open/generate",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/feedback/**",
                    "/result/**",
                    "/questions/**",
                    "/email-verification/**",
                    "/admin/email/**",
                    "/resume/**",
                    "/student/**",
                    "/auth/student/**"
                ).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOriginPatterns(List.of(
        "http://localhost:*",
        "https://elite-apptitude-test-fswdxsy4b-purvanshu-khapres-projects.vercel.app",
        "https://elite-apptitude-test.vercel.app",
        "https://unperpetuating-may-eely.ngrok-free.dev"
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
