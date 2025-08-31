// savinoo/plataforma-mentoria/plataforma-mentoria-BranchTestes/src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/controller/view/AuthController.java
package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.PasswordResetDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.dto.UsuarioFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Usuario;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.UsuarioRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositório para verificar e-mails

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("usuarioDTO", new UsuarioFormDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("usuarioDTO") UsuarioFormDTO usuarioDTO,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes) {
        // Verifica se o e-mail já está em uso
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            result.addError(new FieldError("usuarioDTO", "email", "Este e-mail já está registado."));
        }

        if (result.hasErrors()) {
            return "auth/register";
        }

        // O registo padrão cria um MENTORADO
        Mentorado novoMentorado = new Mentorado();
        novoMentorado.setNome(usuarioDTO.getNome());
        novoMentorado.setEmail(usuarioDTO.getEmail());
        novoMentorado.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        novoMentorado.addPapel("MENTORADO");
        novoMentorado.setAreasDeInteresse("Ainda não definido"); // Valor padrão mais claro

        mentoradoService.saveMentorado(novoMentorado);

        redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça o login.");
        return "redirect:/auth/login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot_password_form";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            String token = UUID.randomUUID().toString();
            usuario.setResetPasswordToken(token);
            usuario.setResetPasswordTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expira em 1 hora
            usuarioRepository.save(usuario);

            // Simulação de envio de e-mail: registo do link no log
            String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;
            logger.info("Link de Redefinição de Senha para " + email + ": " + resetLink);
        }
        // Mostra a mesma página de sucesso, existindo o e-mail ou não, para não revelar informações
        return "auth/forgot_password_success";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model, RedirectAttributes redirectAttributes) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByResetPasswordToken(token);
        if (optionalUsuario.isEmpty() || optionalUsuario.get().getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "O link para redefinição de senha é inválido ou expirou.");
            return "redirect:/auth/login?tokenInvalido=true";
        }

        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setToken(token);
        model.addAttribute("passwordResetDTO", passwordResetDTO);
        return "auth/reset_password_form";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@Valid @ModelAttribute("passwordResetDTO") PasswordResetDTO passwordResetDTO,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        if (!passwordResetDTO.getSenha().equals(passwordResetDTO.getConfirmacaoSenha())) {
            result.addError(new FieldError("passwordResetDTO", "confirmacaoSenha", "As senhas não coincidem."));
        }

        if (result.hasErrors()) {
            return "auth/reset_password_form";
        }

        Optional<Usuario> optionalUsuario = usuarioRepository.findByResetPasswordToken(passwordResetDTO.getToken());
        if (optionalUsuario.isPresent() && optionalUsuario.get().getResetPasswordTokenExpiry().isAfter(LocalDateTime.now())) {
            Usuario usuario = optionalUsuario.get();
            usuario.setSenha(passwordEncoder.encode(passwordResetDTO.getSenha()));
            usuario.setResetPasswordToken(null);
            usuario.setResetPasswordTokenExpiry(null);
            usuario.setTentativasFalhas(0);
            usuario.setTempoBloqueio(null);
            usuarioRepository.save(usuario);

            redirectAttributes.addFlashAttribute("successMessage", "Senha redefinida com sucesso! Pode fazer o login.");
            return "redirect:/auth/login";
        } else {
            return "redirect:/auth/login?tokenInvalido=true";
        }
    }
}