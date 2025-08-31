package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.PedidoMentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.PedidoMentoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;

import java.util.List;

@Controller
@RequestMapping("/mentores")
public class MentorController {
    // ... (propriedades e outros métodos) ...
    @Autowired
    private MentorService mentorService;

    @Autowired
    private PedidoMentoriaService pedidoMentoriaService;
    
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
        
        mentorService.saveMentor(mentorDTO);
        
        redirectAttributes.addFlashAttribute("successMessage", "Sua aplicação foi enviada e será revisada por um administrador.");
        return "redirect:/auth/login";
    }

    @GetMapping("/dashboard")
    public String showMentorDashboard(Authentication authentication, Model model) {
        String email = authentication.getName();
        mentorService.findByEmail(email).ifPresent(mentor -> {
            List<PedidoMentoria> pedidos = pedidoMentoriaService.findPedidosPendentesByMentor(mentor);
            model.addAttribute("pedidosPendentes", pedidos);
        });
        return "mentor/dashboard";
    }

    @PostMapping("/pedidos/{id}/aceitar")
    public String aceitarPedido(@PathVariable("id") Long pedidoId, RedirectAttributes redirectAttributes) {
        pedidoMentoriaService.aceitarPedido(pedidoId);
        redirectAttributes.addFlashAttribute("successMessage", "Pedido de mentoria aceite com sucesso!");
        return "redirect:/mentores/dashboard";
    }

    @PostMapping("/pedidos/{id}/recusar")
    public String recusarPedido(@PathVariable("id") Long pedidoId,
                                @RequestParam("motivo") String motivo, // Recebe o motivo
                                RedirectAttributes redirectAttributes) {
        pedidoMentoriaService.recusarPedido(pedidoId, motivo);
        redirectAttributes.addFlashAttribute("successMessage", "Pedido de mentoria recusado.");
        return "redirect:/mentores/dashboard";
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