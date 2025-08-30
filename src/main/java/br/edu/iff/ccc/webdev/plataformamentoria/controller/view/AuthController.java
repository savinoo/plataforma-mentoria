package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login"; // -> templates/auth/login.html
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        // Lógica para adicionar um objeto de usuário vazio ao modelo, se necessário
        return "auth/register"; // -> templates/auth/register.html
    }

    @PostMapping("/register")
    public String processRegistration() {
        // Lógica de registro (será implementada depois)
        return "redirect:/auth/login";
    }
}