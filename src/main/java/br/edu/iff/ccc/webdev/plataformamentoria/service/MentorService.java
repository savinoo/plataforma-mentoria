// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentorService.java
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public Mentor saveMentor(MentorFormDTO mentorDTO) {
        Mentor mentor = new Mentor();
        mentor.setNome(mentorDTO.getNome());
        mentor.setEmail(mentorDTO.getEmail());
        mentor.setSenha(mentorDTO.getSenha());
        mentor.setEspecialidade(mentorDTO.getEspecialidade());
        mentor.addPapel("MENTOR");
        mentor.setAprovado(false); // Garantir que a aprovação comece como falsa
        return mentorRepository.save(mentor);
    }
    
    // Método para buscar apenas mentores aprovados
    public List<Mentor> findAprovados() {
        return mentorRepository.findByAprovadoIsTrue();
    }

    // Método para buscar um mentor por ID (GET)
    public Optional<Mentor> findMentorById(Long id) {
        return mentorRepository.findById(id);
    }
}