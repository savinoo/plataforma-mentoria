package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mentores")
public class MentorController {

    @Autowired
    private MentorService mentorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showMentoresPage(Model model) {
        model.addAttribute("mentores", mentorService.findAprovados());
        return "mentor/mentores";
    }

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
        
        mentorDTO.setSenha(passwordEncoder.encode(mentorDTO.getSenha()));
        mentorService.saveMentor(mentorDTO);
        
        redirectAttributes.addFlashAttribute("successMessage", "Sua aplicação foi enviada e será revisada por um administrador.");
        return "redirect:/auth/login";
    }
}