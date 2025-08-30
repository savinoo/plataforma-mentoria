// savinoo/plataforma-mentoria/plataforma-mentoria-BranchTestes/src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/controller/view/AdminController.java
package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MentorRepository mentorRepository;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        // Lista mentores que precisam de aprovação
        model.addAttribute("pendingMentors", mentorRepository.findByAprovadoIsFalse());
        return "admin/dashboard";
    }

    @PostMapping("/mentores/{id}/aprovar")
    public String approveMentor(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Mentor mentor = mentorRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de Mentor inválido:" + id));
        mentor.setAprovado(true);
        mentorRepository.save(mentor);
        redirectAttributes.addFlashAttribute("successMessage", "Mentor aprovado com sucesso!");
        return "redirect:/admin/dashboard";
    }
}