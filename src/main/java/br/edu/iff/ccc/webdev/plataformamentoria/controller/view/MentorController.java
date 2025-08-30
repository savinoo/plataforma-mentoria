package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mentores")
public class MentorController {

    @GetMapping("/dashboard")
    public String showMentorDashboard(Model model) {
        // Lógica para buscar solicitações de mentoria pendentes
        return "mentor/dashboard"; // -> templates/mentor/dashboard.html
    }

    @GetMapping("/perfil")
    public String showMentorProfileForm(Model model) {
        // Lógica para buscar os dados do mentor logado para edição
        return "mentor/editar_perfil"; // -> templates/mentor/editar_perfil.html
    }

    @PostMapping("/perfil")
    public String updateMentorProfile() {
        // Lógica para salvar as alterações do perfil
        return "redirect:/mentores/dashboard";
    }
}