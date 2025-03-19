package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))  
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(HttpMethod.POST,"/users/login").permitAll()
            .requestMatchers(HttpMethod.POST,"/users/create").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN") 
            .anyRequest().authenticated()
            );
           // .httpBasic(org.springframework.security.config.Customizer.withDefaults());  

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    
}
