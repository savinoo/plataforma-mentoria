package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Usuario;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.UsuarioRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.service.PedidoMentoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoMentoriaService pedidoMentoriaService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("pendingMentors", mentorRepository.findByAprovadoIsFalse());
        
        long totalRecomendacoes = pedidoMentoriaService.getTotalPedidosDeRecomendacoes();
        long aceitosRecomendacoes = pedidoMentoriaService.getTotalPedidosAceitosDeRecomendacoes();

        model.addAttribute("totalRecomendacoes", totalRecomendacoes);
        model.addAttribute("aceitosRecomendacoes", aceitosRecomendacoes);
        
        double taxaSucesso = (totalRecomendacoes > 0) ? ((double) aceitosRecomendacoes / totalRecomendacoes) * 100 : 0;
        model.addAttribute("taxaSucessoRecomendacoes", taxaSucesso);

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
    
    @GetMapping("/usuarios")
    public String listUsers(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/toggle-suspend")
    public String toggleSuspendUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de Usuário inválido:" + id));

        if (usuario.getTempoBloqueio() == null) {
            usuario.setTempoBloqueio(LocalDateTime.now().plusYears(100));
            redirectAttributes.addFlashAttribute("successMessage", "Usuário suspenso com sucesso.");
        } else {
            usuario.setTempoBloqueio(null);
            usuario.setTentativasFalhas(0);
            redirectAttributes.addFlashAttribute("successMessage", "Suspensão do usuário removida.");
        }
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/toggle-ban")
    public String toggleBanUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de Usuário inválido:" + id));
        
        usuario.setBanido(!usuario.isBanido());
        if (usuario.isBanido()) {
            redirectAttributes.addFlashAttribute("successMessage", "Usuário banido com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Banimento do usuário removido.");
        }
        usuarioRepository.save(usuario);
        return "redirect:/admin/usuarios";
    }
}