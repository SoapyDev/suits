package com.backend.suits.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {
    public static final String ROLE_PROFESSIONNEL = "ROLE_PROFESSIONNEL";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        )
                            .permitAll()
                        .requestMatchers(
                                "/",
                                "/login/**",
                                "/login/register",
                                "/api/organisation",
                                "/register/professional",
                                "/register/client",
                                "/api/convention/get/**"
                        )
                            .permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/client/**"
                        )
                            .hasAuthority(ROLE_CLIENT)
//                        .requestMatchers(
//                                HttpMethod.POST,
//                                "/api/convention/accept",
//                                "/api/dossier/**"
//                        )
//                            .hasAuthority(ROLE_CLIENT)
                        .requestMatchers(
                                "/professionnel/**",
                                "/api/**"
                        )
                            .hasAnyAuthority(ROLE_PROFESSIONNEL, ROLE_CLIENT)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .logout(logout -> logout
                .logoutUrl("/logout")
        );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
