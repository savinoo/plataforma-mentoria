// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/config/SecurityConfig.java
package br.edu.iff.ccc.webdev.plataformamentoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rotas públicas para todos os visitantes
                .requestMatchers("/", "/home", "/auth/**", "/css/**", "/js/**", "/h2-console/**").permitAll()
                // Permite que novos mentores se registem
                .requestMatchers(HttpMethod.GET, "/mentores/new").permitAll()
                .requestMatchers(HttpMethod.POST, "/mentores").permitAll()
                // Rotas específicas para cada papel (ROLE)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/mentores/dashboard", "/mentores/perfil/**", "/mentores/pedidos/**").hasRole("MENTOR")
                .requestMatchers("/mentorados/**").hasRole("MENTORADO")
                // Todas as outras requisições exigem autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler)
                .failureUrl("/auth/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/auth/sso/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}