package com.workoutplanner.MiniProject.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collections;


// Cau hinh Spring Security
@Configuration
@EnableWebSecurity
@CrossOrigin(origins = "http://localhost:5173")
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/api/v1/auth", "/api/v1/workout-plans", "/api/v1/auth/logout", "/api/v1/auth/introspect", "/api/v1/auth/refresh"};
    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Cau hinh quyen truy cap endpoint
        // Neu URL trong danh sach -> cho qua
        // Bao ve khoi attack
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(request ->
                        // Cho phep truy cap
                request.requestMatchers("/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST,PUBLIC_ENDPOINTS).permitAll()
                        // Spring Security kiểm tra quyền truy cập
                        // Spring Security không hiểu "ADMIN" trực tiếp là quyền, vì hasRole() hoặc hasAuthority() cần GrantedAuthority
                        // Spring sẽ xem GrantedAuthority của user (ROLE_USER) có khớp "ROLE_ADMIN" không → không khớp → 403.
                        .requestMatchers(HttpMethod.GET,"/api/v1/users").hasRole("ADMIN")
                        .anyRequest().authenticated());

        // Neu ko -> Tim token JWT de xac minh, token hop le -> cho qua
        // Giai ma token -> kiem tra chu ky -> xac dinh authorization
        // JwtAuthenticationConverter chuyen "role" trong Claim thanh GrantedAuthority

        httpSecurity.oauth2ResourceServer(oath2 ->
                oath2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder).jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return httpSecurity.build();
    }

//    @Bean
//    JwtDecoder jwtDecoder() { --> Ko ap dung duoc voi login vi khi token logout roi ko dc access vo nua
//        // tao 1 khoa bi mat tu signerKey
//        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//
//        // Giai ma JWT -> Xac minh chu ky -> Lay claim
//        return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    // Converter giúp Spring Security chuyển JWT token → Authentication object.
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            String role = jwt.getClaimAsString("role"); // đọc claim "role" tu token
            if (role != null) {
                // Tạo GrantedAuthority cho Spring Security
                // Chuyển "USER" → "ROLE_USER" (tạo GrantedAuthority).
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return Collections.emptyList();
        });

        return converter;
    }
}