package BankingManagementSystem.BankingManagementSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Exclude Swagger-related URLs from authentication
                .requestMatchers("/swagger-ui/**", "/v2/api-docs", "/swagger-resources/**", "/webjars/**").permitAll()
                // Allow registration API to be accessed without authentication
                .requestMatchers(HttpMethod.POST, "/api/customers/register").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()  // Allow everyone to access the form login
                .and()
                .httpBasic();  // Enable basic authentication for other endpoints
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Use BCryptPasswordEncoder for encoding passwords
    }
}
