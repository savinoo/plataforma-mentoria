package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        return "admin/dashboard"; // -> templates/admin/dashboard.html
    }

    @GetMapping("/usuarios")
    public String listUsers(Model model) {
        // Lógica para listar todos os usuários (mentores e mentorados)
        return "admin/lista_usuarios"; // -> templates/admin/lista_usuarios.html
    }
}