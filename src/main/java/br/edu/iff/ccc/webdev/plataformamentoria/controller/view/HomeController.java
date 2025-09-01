package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping()
public class HomeController {

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showHomePage(Authentication authentication) {
        // Se o utilizador não estiver autenticado, mostra a página inicial pública.
        if (authentication == null || !authentication.isAuthenticated()) {
            return "home";
        }

        // Se estiver autenticado, redireciona para o dashboard correspondente.
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MENTOR"))) {
            return "redirect:/mentores/dashboard";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MENTORADO"))) {
            return "redirect:/mentorados/dashboard";
        }
        
        // Fallback para a página inicial se nenhum papel corresponder.
        return "home";
    }
}