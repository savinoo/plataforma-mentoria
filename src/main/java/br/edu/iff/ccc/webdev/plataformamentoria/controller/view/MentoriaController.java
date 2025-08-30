// src/main/java/br/edu/iff/mentorplatform/controller/view/MentoriaController.java
package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentoriaFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService; // Crie este serviço
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mentorias")
public class MentoriaController {

    @Autowired private MentoriaService mentoriaService;
    @Autowired private MentorService mentorService;
    @Autowired private MentoradoService mentoradoService; // Crie um serviço simples para mentorado

    @GetMapping
    public String showMentorias(Model model) {
        model.addAttribute("mentorias", mentoriaService.findAll());
        return "mentoria/mentorias"; // -> templates/mentoria/mentorias.html
    }

    @GetMapping("/new")
    public String showNewMentoriaForm(Model model) {
        model.addAttribute("mentoria", new MentoriaFormDTO());
        model.addAttribute("allMentores", mentorService.findAllMentores());
        model.addAttribute("allMentorados", mentoradoService.findAllMentorados());
        return "mentoria/mentoria_form"; // -> templates/mentoria/mentoria_form.html
    }
    
    @PostMapping
    public String saveMentoria(@Valid @ModelAttribute("mentoria") MentoriaFormDTO mentoriaDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Recarrega os dados necessários para o formulário em caso de erro
            model.addAttribute("allMentores", mentorService.findAllMentores());
            model.addAttribute("allMentorados", mentoradoService.findAllMentorados());
            return "mentoria/mentoria_form";
        }
        mentoriaService.save(mentoriaDTO);
        return "redirect:/mentorias";
    }
}