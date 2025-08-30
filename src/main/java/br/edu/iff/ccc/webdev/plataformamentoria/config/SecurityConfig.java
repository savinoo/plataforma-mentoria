
// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/config/SecurityConfig.java
package br.edu.iff.ccc.webdev.plataformamentoria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Permite acesso público a estas URLs
                .requestMatchers("/", "/home", "/auth/register", "/css/**", "/js/**", "/h2-console/**").permitAll()
                // Admin tem acesso a /admin/**
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Mentor tem acesso a seu painel
                .requestMatchers("/mentores/dashboard").hasRole("MENTOR")
                // Mentorado tem acesso a seu painel e busca
                .requestMatchers("/mentorados/**").hasRole("MENTORADO")
                // Qualquer outra requisição exige autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/auth/login") // Página de login customizada
                .defaultSuccessUrl("/home", true) // Redireciona após login sucesso
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/auth/login?logout") // Mostra mensagem de logout
                .permitAll()
            )
            // Configurações para o H2 Console
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}