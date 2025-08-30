// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/controller/view/MentorController.java
package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private PasswordEncoder passwordEncoder; // Injetar o encoder

    // Exibe a lista de todos os mentores (GET)
    @GetMapping
    public String showMentoresPage(Model model) {
        model.addAttribute("mentores", mentorService.findAprovados());
        return "mentor/mentores"; // -> templates/mentor/mentores.html
    }

    // Exibe o formulário para adicionar um novo mentor (GET)
    @GetMapping("/new")
    public String showNewMentorForm(Model model) {
        model.addAttribute("mentor", new MentorFormDTO());
        return "mentor/mentor_form"; // -> templates/mentor/mentor_form.html
    }

    // Processa o envio do formulário para salvar o mentor (POST)
    @PostMapping
    public String saveMentor(@Valid @ModelAttribute("mentor") MentorFormDTO mentorDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "mentor/mentor_form";
        }
        
        // Criptografar a senha e adicionar o papel de MENTOR
        mentorDTO.setSenha(passwordEncoder.encode(mentorDTO.getSenha()));
        mentorService.saveMentor(mentorDTO);
        
        // A lógica no service já deve cuidar de setar `aprovado = false`
        redirectAttributes.addFlashAttribute("successMessage", "Sua aplicação foi enviada e será revisada por um administrador.");
        return "redirect:/home"; // Redirecionar para home após aplicação
    }
}