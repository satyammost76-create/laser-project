package alphalaser.cfc.laser_project.Configration;

import alphalaser.cfc.laser_project.Components.JwtFilter;
import alphalaser.cfc.laser_project.Service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    // PASSWORD ENCODER

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION MANAGER

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    // SECURITY FILTER

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // DISABLE CSRF

                .csrf(csrf -> csrf.disable())

                // ENABLE CORS

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource
                        )
                )

                // STATELESS SESSION

                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // URL AUTHORIZATION

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC FILES

                        .requestMatchers(

                                "/",
                                "/index.html",
                                "/admin.html",
                                "/user.html",

                                "/logo.png",
                                "/machine.png",
                                "/photo.png",

                                "/css/**",
                                "/js/**",

                                "/favicon.ico",

                                "/rajeshroy/auth/**"

                        ).permitAll()

                        // SECURE OTHER APIS

                        .anyRequest().authenticated()
                )

                // USER DETAILS SERVICE

                .userDetailsService(userDetailsService)

                // JWT FILTER

                .addFilterBefore(

                        jwtFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}