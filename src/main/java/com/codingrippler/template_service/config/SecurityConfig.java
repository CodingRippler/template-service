package com.codingrippler.template_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.codingrippler.template_service.enums.ErrorCode;
import com.codingrippler.template_service.exception.CustomException;

@Configuration
@EnableWebSecurity
@DependsOn("configurationInitializer")
@Profile("!test")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/commerce/**").permitAll() // Public endpoints
                        .requestMatchers("/actuator/**").permitAll() // Public actuator endpoints
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Stateless for
        // JWT
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // In-memory user for demonstration purposes

        String username = ConfigurationCache.getConfigValue("app.security.username");
        String password = ConfigurationCache.getConfigValue("app.security.password");

        validateProperty(username, "app.security.username");
        validateProperty(password, "app.security.password");

        return new InMemoryUserDetailsManager(
                User.withUsername(ConfigurationCache.getConfigValue("app.security.username"))
                        .password(passwordEncoder().encode(ConfigurationCache.getConfigValue("app.security.password")))
                        .roles("ADMIN")
                        .build());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void validateProperty(String value, String propertyName) {
        if (value == null || value.isEmpty()) {
            throw new CustomException(ErrorCode.CONFIGURATION_ERROR,
                    "Missing required configuration property: " + propertyName);
        }
    }
}
