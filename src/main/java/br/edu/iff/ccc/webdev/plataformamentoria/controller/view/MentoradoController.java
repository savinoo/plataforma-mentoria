package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mentorados")
public class MentoradoController {

    @GetMapping("/dashboard")
    public String showMentoradoDashboard(Model model) {
        // Lógica para buscar as mentorias agendadas do mentorado
        return "mentorado/dashboard"; // -> templates/mentorado/dashboard.html
    }

    @GetMapping("/busca")
    public String searchMentores(Model model) {
        // Lógica para buscar e listar todos os mentores
        return "mentorado/busca_mentores"; // -> templates/mentorado/busca_mentores.html
    }
    
    @GetMapping("/mentores/{id}")
    public String showMentorProfile(@PathVariable("id") Long id, Model model) {
        // Lógica para buscar um mentor pelo ID e exibi-lo
        return "mentor/perfil_publico"; // -> templates/mentor/perfil_publico.html
    }

    @PostMapping("/solicitar")
    public String requestMentoria() {
        // Lógica para processar a solicitação de mentoria
        return "redirect:/mentorados/dashboard?success";
    }
}