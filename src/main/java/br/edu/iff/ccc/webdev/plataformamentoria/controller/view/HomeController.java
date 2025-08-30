package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping() // Removido o "/home" daqui para o controller responder na raiz
public class HomeController {

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home"; // Redireciona a raiz para /home
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home"; // Nome do arquivo HTML sem a extensão
    }
}