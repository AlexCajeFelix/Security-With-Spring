package com.example.demo.Filter;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.demo.Services.CustomUserDetailsService;
import com.example.demo.TokenService.GeneretedToken;

import ch.qos.logback.classic.Logger;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Service
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private GeneretedToken tokenService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(SecurityFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        // Log para verificar o JWT recebido
        if (jwt != null) {
            logger.info("JWT Received: " + jwt);

            String subject = tokenService.getSubject(jwt);
            
            logger.info("JWT Subject: " + subject);

            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
            // Log para verificar os detalhes do usuário
            logger.info("UserDetails: " + userDetails.getUsername() + ", Authorities: " + userDetails.getAuthorities());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("SecurityContext Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        } else {
            logger.warn("No JWT token found in request.");
        }

        filterChain.doFilter(request, response);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Retorna o token sem o prefixo "Bearer "
        }
        return null;
    }
}
