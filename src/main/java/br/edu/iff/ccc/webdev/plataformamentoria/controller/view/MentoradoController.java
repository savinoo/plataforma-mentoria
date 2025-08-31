package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/mentorados")
public class MentoradoController {

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private MentorService mentorService; // Service do Mentor injetado

    @GetMapping("/dashboard")
    public String showMentoradoDashboard(Model model) {
        return "mentorado/dashboard"; 
    }

    @GetMapping("/busca")
    public String searchMentores(Model model) {
        // CORREÇÃO: Busca apenas mentores aprovados para listar
        model.addAttribute("mentores", mentorService.findAprovados());
        return "mentorado/busca_mentores";
    }
    
    @GetMapping("/mentores/{id}")
    public String showMentorProfile(@PathVariable("id") Long id, Model model) {
        // CORREÇÃO: Busca o mentor pelo ID e o adiciona ao modelo
        Mentor mentor = mentorService.findMentorById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de Mentor inválido:" + id));
        model.addAttribute("mentor", mentor);
        return "mentor/perfil_publico";
    }

    @PostMapping("/solicitar")
    public String requestMentoria() {
        return "redirect:/mentorados/dashboard?success";
    }

    @GetMapping("/onboarding")
    public String showOnboardingPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Optional<Mentorado> mentoradoOpt = mentoradoService.findByEmail(currentUserName);

        if (mentoradoOpt.isPresent()) {
            model.addAttribute("mentorado", mentoradoOpt.get());
        } else {
            return "redirect:/error";
        }
        return "mentorado/onboarding";
    }

    @PostMapping("/onboarding")
    public String processOnboarding(@ModelAttribute Mentorado mentoradoAtualizado, RedirectAttributes redirectAttributes) {
        mentoradoService.updateOnboarding(mentoradoAtualizado);
        redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
        return "redirect:/mentorados/dashboard";
    }
}