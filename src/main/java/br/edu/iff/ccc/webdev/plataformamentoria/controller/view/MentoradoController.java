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

import java.util.List;
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
    public String searchMentores(
            @RequestParam(value = "termo", required = false) String termo,
            @RequestParam(value = "especialidade", required = false) String especialidade,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", required = false, defaultValue = "relevancia") String sort,
            Model model) {
        
        List<Mentor> mentoresEncontrados = mentorService.searchMentores(termo, especialidade, status, sort);
        List<String> todasEspecialidades = mentorService.getEspecialidades();

        model.addAttribute("mentores", mentoresEncontrados);
        model.addAttribute("especialidades", todasEspecialidades);
        
        // Devolve os valores dos filtros para a view
        model.addAttribute("termo", termo);
        model.addAttribute("especialidadeSelecionada", especialidade);
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("sortSelecionado", sort);
        
        return "mentorado/busca_mentores";
    }

    // ... (restante dos métodos do controller) ...
    @GetMapping("/mentores/{id}")
    public String showMentorProfile(@PathVariable("id") Long id, Model model) {
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