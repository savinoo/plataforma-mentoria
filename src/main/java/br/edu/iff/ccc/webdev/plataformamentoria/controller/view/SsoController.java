package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.UsuarioRepository; // Importar o repositório
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/auth/sso")
public class SsoController {

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private UsuarioRepository usuarioRepository; // Adicionar o repositório de usuário

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("usuarioService")
    private UserDetailsService userDetailsService;

    @GetMapping("/login")
    public String redirectToSso() {
        return "auth/sso_login_mock";
    }

    @PostMapping("/callback")
    public String handleSsoCallback(@RequestParam String nome, @RequestParam String email, RedirectAttributes redirectAttributes) {
        
        // Lógica CORRIGIDA: Verifica se o utilizador já existe
        if (usuarioRepository.findByEmail(email).isEmpty()) {
            // Se não existe, cria um novo mentorado
            Mentorado novoMentorado = new Mentorado();
            novoMentorado.setNome(nome);
            novoMentorado.setEmail(email);
            novoMentorado.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
            novoMentorado.addPapel("MENTORADO");
            novoMentorado.setAreasDeInteresse("Ainda não definido");

            mentoradoService.saveMentorado(novoMentorado);
            redirectAttributes.addFlashAttribute("successMessage", "Registo via SSO bem-sucedido! Complete o seu perfil.");
        }

        // Autentica o utilizador (seja ele novo ou já existente)
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // O CustomAuthenticationSuccessHandler fará o redirecionamento correto
        return "redirect:/home";
    }
}