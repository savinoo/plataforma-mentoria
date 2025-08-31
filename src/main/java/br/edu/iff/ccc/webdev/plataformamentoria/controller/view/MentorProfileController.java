package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mentor/perfil")
public class MentorProfileController {

    @Autowired
    private MentorService mentorService;

    // Opções pré-definidas para as áreas de especialização
    private final List<String> areasList = List.of(
        "Transição de Carreira", 
        "Preparação para Entrevistas", 
        "Investigação Académica",
        "Desenvolvimento de Software",
        "Gestão de Produtos",
        "Empreendedorismo"
    );

    @GetMapping
    public String showProfileForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Mentor mentor = mentorService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        
        model.addAttribute("mentor", mentor);
        model.addAttribute("areasList", areasList);
        return "mentor/perfil_form";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute Mentor mentor, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        mentorService.updateProfile(email, mentor);
        redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
        return "redirect:/mentor/perfil";
    }
}