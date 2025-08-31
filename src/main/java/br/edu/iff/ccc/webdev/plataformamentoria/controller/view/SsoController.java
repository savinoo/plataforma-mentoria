package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // Importar Qualifier
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("usuarioService") // Especifica qual bean injetar
    private UserDetailsService userDetailsService;

    // Etapa 1: Redireciona para a página de login simulada do SSO.
    @GetMapping("/login")
    public String redirectToSso() {
        // Na vida real, isto redirecionaria para o provedor de SSO da instituição.
        return "auth/sso_login_mock";
    }

    // Etapa 2: Processa o "retorno" do SSO.
    @PostMapping("/callback")
    public String handleSsoCallback(@RequestParam String nome, @RequestParam String email, RedirectAttributes redirectAttributes) {
        // Simula a criação de um usuário com dados verificados do SSO.
        Mentorado novoMentorado = new Mentorado();
        novoMentorado.setNome(nome);
        novoMentorado.setEmail(email);
        // Gera uma senha aleatória e segura, já que o login será via SSO.
        novoMentorado.setSenha(passwordEncoder.encode(UUID.randomUUID().toString()));
        novoMentorado.addPapel("MENTORADO");
        novoMentorado.setInteresses("Ainda não definido"); // Valor padrão

        mentoradoService.saveMentorado(novoMentorado);

        // Autentica o usuário automaticamente na plataforma após o registo via SSO.
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        redirectAttributes.addFlashAttribute("successMessage", "Autenticação SSO bem-sucedida! Complete o seu perfil.");

        // Etapa 3: Redireciona para o onboarding.
        return "redirect:/mentorados/onboarding";
    }
}