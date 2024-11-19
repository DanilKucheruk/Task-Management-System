package com.tsm.configs;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tsm.util.JwtTokenUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * A filter that intercepts incoming HTTP requests to validate JWT tokens and set the security context.
 * 
 * <p>This filter is executed once per request and extracts user information from the JWT
 * provided in the `Authorization` header. If the token is valid, it authenticates the user
 * by setting an {@link Authentication} object in the {@link SecurityContextHolder}.</p>
 * 
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Parses and validates JWT tokens from the `Authorization` header.</li>
 *   <li>Extracts the username and role from the token.</li>
 *   <li>Handles expired or invalid JWT tokens gracefully with logging.</li>
 * </ul>
 * 
 * <p><b>Dependencies:</b></p>
 * <ul>
 *   <li>{@link JwtTokenUtils}: Utility class for working with JWT tokens.</li>
 *   <li>Lombok annotations for dependency injection ({@code @RequiredArgsConstructor}) and logging ({@code @Slf4j}).</li>
 * </ul>
 * 
 * <p><b>Security Note:</b> Ensure that JWT tokens are properly signed and validated
 * to prevent unauthorized access or tampering.</p>
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFiler extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Filters incoming HTTP requests to process and validate JWT tokens.
     * 
     * <p>This method performs the following steps:</p>
     * <ul>
     *   <li>Extracts the `Authorization` header from the request.</li>
     *   <li>Parses the JWT token if it starts with "Bearer ".</li>
     *   <li>Validates the token and extracts the username and role.</li>
     *   <li>Creates an {@link Authentication} object with the extracted user details and sets it in the {@link SecurityContextHolder}.</li>
     *   <li>Logs errors if the token is expired or the signature is invalid.</li>
     * </ul>
     * 
     * @param request the {@link HttpServletRequest} containing the client request
     * @param response the {@link HttpServletResponse} containing the server response
     * @param filterChain the {@link FilterChain} to pass the request and response to the next filter
     * @throws ServletException if an error occurs during filtering
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        String role = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                // Extract username and role from the token
                username = jwtTokenUtils.extractUsername(jwt);
                role = jwtTokenUtils.extractClaim(jwt, claims -> claims.get("role", String.class));
                // Create authorities and set authentication
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                log.debug("Token has expired.");
            } catch (SignatureException e) {
                log.debug("Invalid token signature.");
            }
        }
        // Ensure the security context is updated if the username is not null
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
            Authentication token = new UsernamePasswordAuthenticationToken(
                 username,
                    null,
                    authorities);
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        // Proceed with the next filter in the chain
        filterChain.doFilter(request, response);
    }

}
