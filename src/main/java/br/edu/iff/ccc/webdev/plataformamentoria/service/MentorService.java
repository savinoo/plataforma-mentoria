// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentorService.java
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;
    
    @Transactional
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
    
    public List<Mentor> findAprovados() {
        return mentorRepository.findByAprovadoIsTrue();
    }

    public Optional<Mentor> findMentorById(Long id) {
        return mentorRepository.findById(id);
    }

    public Optional<Mentor> findByEmail(String email) {
        return mentorRepository.findByEmail(email);
    }

    @Transactional
    public void updateProfile(String email, Mentor profileData) {
        Mentor mentor = mentorRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Mentor não encontrado com o email: " + email));

        mentor.setResumoProfissional(profileData.getResumoProfissional());
        mentor.setFilosofiaMentoria(profileData.getFilosofiaMentoria());
        mentor.setCompetencias(profileData.getCompetencias());
        mentor.setAreasDeEspecializacao(profileData.getAreasDeEspecializacao());
        
        mentorRepository.save(mentor);
    }
}