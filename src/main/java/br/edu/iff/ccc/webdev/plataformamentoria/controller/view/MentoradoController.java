package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.RecomendacaoDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.PedidoMentoriaService;
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
    private MentorService mentorService;
    
    @Autowired
    private PedidoMentoriaService pedidoMentoriaService;

    @GetMapping("/dashboard")
    public String showMentoradoDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        mentoradoService.findByEmail(email).ifPresent(mentorado -> {
            model.addAttribute("mentorado", mentorado);
            if (mentorado.isOnboardingCompleto()) {
                List<RecomendacaoDTO> recomendacoes = mentorService.recomendarMentores(mentorado);
                model.addAttribute("recomendacoes", recomendacoes);
            }
            // Adiciona a lista de pedidos enviados ao dashboard
            model.addAttribute("pedidosEnviados", pedidoMentoriaService.findPedidosByMentorado(mentorado));
        });
        return "mentorado/dashboard";
    }

    @PostMapping("/solicitar-mentoria")
    public String solicitarMentoria(@RequestParam("mentorId") Long mentorId,
                                    @RequestParam("mensagem") String mensagem,
                                    @RequestParam(value = "isRecomendacao", defaultValue = "false") boolean isRecomendacao,
                                    Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        Mentorado mentorado = mentoradoService.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mentorado não encontrado"));
        Mentor mentor = mentorService.findMentorById(mentorId)
            .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        
        try {
            pedidoMentoriaService.criarPedido(mentorado, mentor, mensagem, isRecomendacao);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido de mentoria enviado com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/mentorados/mentores/" + mentorId;
    }

    // ... (restante dos métodos) ...
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
        
        model.addAttribute("termo", termo);
        model.addAttribute("especialidadeSelecionada", especialidade);
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("sortSelecionado", sort);
        
        return "mentorado/busca_mentores";
    }

    @GetMapping("/mentores/{id}")
    public String showMentorProfile(@PathVariable("id") Long id, Model model) {
        Mentor mentor = mentorService.findMentorById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de Mentor inválido:" + id));
        model.addAttribute("mentor", mentor);
        return "mentor/perfil_publico";
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