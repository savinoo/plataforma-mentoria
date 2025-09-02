package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_DURATION_MINUTES = 15;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) {
        String email = (String) event.getAuthentication().getPrincipal();
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            usuario.setTentativasFalhas(usuario.getTentativasFalhas() + 1);
            if (usuario.getTentativasFalhas() >= MAX_ATTEMPTS) {
                usuario.setTempoBloqueio(LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES));
            }
            usuarioRepository.save(usuario);
        });
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String email = event.getAuthentication().getName();
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            if (usuario.getTentativasFalhas() > 0 || usuario.getTempoBloqueio() != null) {
                usuario.setTentativasFalhas(0);
                usuario.setTempoBloqueio(null);
                usuarioRepository.save(usuario);
            }
        });
    }
}