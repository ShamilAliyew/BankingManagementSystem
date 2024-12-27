package BankingManagementSystem.BankingManagementSystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Yetkilendirme kuralları
                .authorizeRequests()
                // Swagger URL'lerine erişim izni
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                // Kayıt (register) endpoint'ine izin
                .requestMatchers(HttpMethod.POST, "/api/customers/register").permitAll()
                // Diğer tüm istekler için kimlik doğrulama
                .anyRequest().authenticated()
                .and()
                // Form tabanlı giriş ekranını etkinleştir
                .formLogin()
                .permitAll()
                .and()
                // HTTP Basic kimlik doğrulamayı etkinleştir
                .httpBasic()
                .and()
                // CSRF koruması
                .csrf().disable(); // Geliştirme sırasında CSRF'yi devre dışı bırakabilirsiniz
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Güçlü bir şifreleme mekanizması kullan
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin")) // Parola bcrypt ile şifrelenir
                .roles("USER") // Kullanıcıya "USER" rolü atanır
                .build();
        return new InMemoryUserDetailsManager(user);
    }

}
