// CORREÇÃO PARA:
// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/config/DataInitializer.java

package br.edu.iff.ccc.webdev.plataformamentoria.config;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor; // Importar a classe Mentor
import br.edu.iff.ccc.webdev.plataformamentoria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cria um admin se não existir
        if (usuarioRepository.findByEmail("admin@plataforma.com").isEmpty()) {
            
            // **A CORREÇÃO ESTÁ AQUI**
            // Usamos a entidade concreta 'Mentor' para criar o usuário admin.
            Mentor admin = new Mentor(); 
            
            admin.setNome("Admin");
            admin.setEmail("admin@plataforma.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.addPapel("ADMIN"); // Adicionamos o papel de administrador
            admin.setEspecialidade("Administração do Sistema");
            admin.setAprovado(true); // O administrador já deve ser aprovado
            
            usuarioRepository.save(admin);
        }
    }
}