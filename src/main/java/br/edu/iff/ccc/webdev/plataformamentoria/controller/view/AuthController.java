// savinoo/plataforma-mentoria/plataforma-mentoria-BranchTestes/src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/controller/view/AuthController.java
package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.UsuarioFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

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
        novoMentorado.setInteresses("Ainda não definido"); // Valor padrão mais claro

        mentoradoService.saveMentorado(novoMentorado);

        redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça o login.");
        return "redirect:/auth/login";
    }
}