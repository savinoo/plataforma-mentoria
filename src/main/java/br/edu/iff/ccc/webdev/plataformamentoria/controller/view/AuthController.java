// savinoo/plataforma-mentoria/plataforma-mentoria-BranchTestes/src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/controller/view/AuthController.java
package br.edu.iff.ccc.webdev.plataformamentoria.controller.view;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.UsuarioFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
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
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if (result.hasErrors()) {
            return "auth/register";
        }

        // Por enquanto, o registro padrão cria um MENTORADO
        Mentorado novoMentorado = new Mentorado();
        novoMentorado.setNome(usuarioDTO.getNome());
        novoMentorado.setEmail(usuarioDTO.getEmail());
        novoMentorado.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        novoMentorado.addPapel("MENTORADO");
        novoMentorado.setInteresses(""); // Interesses podem ser preenchidos depois

        mentoradoService.saveMentorado(novoMentorado);

        redirectAttributes.addFlashAttribute("successMessage", "Cadastro realizado com sucesso! Faça o login.");
        return "redirect:/auth/login";
    }
}