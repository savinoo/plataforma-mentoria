package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mentores")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    // MELHORIA: PasswordEncoder não é mais necessário aqui.
    
    private final List<String> areasList = List.of(
        "Transição de Carreira", "Preparação para Entrevistas", "Investigação Académica",
        "Desenvolvimento de Software", "Gestão de Produtos", "Empreendedorismo"
    );

    private final List<String> formatosList = List.of("Online", "Presencial", "Ambos");

    @GetMapping("/new")
    public String showNewMentorForm(Model model) {
        model.addAttribute("mentor", new MentorFormDTO());
        return "mentor/mentor_form";
    }

    @PostMapping
    public String saveMentor(@Valid @ModelAttribute("mentor") MentorFormDTO mentorDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        if (!mentorDTO.getSenha().equals(mentorDTO.getConfirmacaoSenha())) {
            result.addError(new FieldError("mentor", "confirmacaoSenha", "As senhas não coincidem."));
        }

        if (result.hasErrors()) {
            return "mentor/mentor_form";
        }
        
        // MELHORIA: A codificação da senha foi removida daqui.
        mentorService.saveMentor(mentorDTO);
        
        redirectAttributes.addFlashAttribute("successMessage", "Sua aplicação foi enviada e será revisada por um administrador.");
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String showMentorDashboard() {
        return "mentor/dashboard";
    }

    @GetMapping("/perfil")
    public String showProfileForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        Mentor mentor = mentorService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        
        model.addAttribute("mentor", mentor);
        model.addAttribute("areasList", areasList);
        model.addAttribute("formatosList", formatosList);
        return "mentor/perfil_form";
    }

    @PostMapping("/perfil")
    public String updateProfile(@ModelAttribute Mentor mentor, Authentication authentication, RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        mentorService.updateProfile(email, mentor);
        redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
        return "redirect:/mentores/perfil";
    }
}