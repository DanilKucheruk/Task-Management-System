package com.tsm.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tsm.service.UserService;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    private final JwtRequestFiler requestFiler;

    /**
     * Configures a {@link DaoAuthenticationProvider} for authenticating users.
     * 
     * <p>This provider:</p>
     * <ul>
     *   <li>Uses {@link UserService} to load user details from the database.</li>
     *   <li>Encodes passwords using {@link BCryptPasswordEncoder}.</li>
     * </ul>
     * 
     * @param userService the service responsible for retrieving user details
     * @param passwordEncoder the encoder used to hash passwords
     * @return a configured {@link DaoAuthenticationProvider}
     */

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserService userService,
            BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    /**
     * Provides a bean for encoding passwords using BCrypt.
     * 
     * @return an instance of {@link BCryptPasswordEncoder}
     */
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the {@link AuthenticationManager}, which is used for authentication operations.
     * 
     * @param authenticationConfiguration the Spring Security configuration for authentication
     * @return a configured {@link AuthenticationManager}
     * @throws Exception if there is an error during configuration
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the {@link SecurityFilterChain} for defining security rules and behavior.
     * 
     * <p>This configuration:</p>
     * <ul>
     *   <li>Disables CSRF for stateless authentication.</li>
     *   <li>Permits access to certain endpoints (e.g., Swagger UI, registration, and authentication).</li>
     *   <li>Requires authentication for all other endpoints.</li>
     *   <li>Sets up a stateless session policy using {@link SessionCreationPolicy#STATELESS}.</li>
     *   <li>Adds the {@link JwtRequestFiler} for validating JWT tokens before the {@link UsernamePasswordAuthenticationFilter}.</li>
     *   <li>Configures a custom {@link HttpStatusEntryPoint} to return HTTP 401 (Unauthorized) for unauthorized access.</li>
     * </ul>
     * 
     * @param http the {@link HttpSecurity} object for configuring security rules
     * @return a configured {@link SecurityFilterChain}
     * @throws Exception if there is an error during configuration
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/registration",
                                "/api/auth",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/sescurity",
                                "/webjars/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-docs/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(requestFiler, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}