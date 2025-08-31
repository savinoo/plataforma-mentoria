package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentoradoRepository;
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

    // Novo método para a página de onboarding
    @GetMapping("/onboarding")
    public String showOnboardingPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Optional<Mentorado> mentoradoOpt = mentoradoService.findByEmail(currentUserName);

        if (mentoradoOpt.isPresent()) {
            model.addAttribute("mentorado", mentoradoOpt.get());
        } else {
            // Lida com o caso de não encontrar o mentorado, talvez redirecionando para o erro
            return "redirect:/error";
        }
        return "mentorado/onboarding"; // -> templates/mentorado/onboarding.html
    }

    @PostMapping("/onboarding")
    public String processOnboarding(@ModelAttribute Mentorado mentoradoAtualizado, RedirectAttributes redirectAttributes) {
        mentoradoService.updateOnboarding(mentoradoAtualizado);
        redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
        return "redirect:/mentorados/dashboard";
    }
}