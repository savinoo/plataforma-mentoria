// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentorService.java
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    // Método para salvar (criar ou atualizar) um mentor (POST)
    public Mentor saveMentor(Mentor mentor) {
        // Futuramente, pode incluir validações, como verificar se o email já existe
        return mentorRepository.save(mentor);
    }
    
    // Novo método para salvar a partir de um DTO
    public Mentor saveMentor(MentorFormDTO mentorDTO) {
        Mentor mentor = new Mentor();
        mentor.setNome(mentorDTO.getNome());
        mentor.setEmail(mentorDTO.getEmail());
        mentor.setSenha(mentorDTO.getSenha()); // Em um projeto real, a senha seria criptografada aqui
        mentor.setEspecialidade(mentorDTO.getEspecialidade());
        return mentorRepository.save(mentor);
    }

    // Método para buscar todos os mentores (GET)
    public List<Mentor> findAllMentores() {
        return mentorRepository.findAll();
    }

    // Método para buscar um mentor por ID (GET)
    public Optional<Mentor> findMentorById(Long id) {
        return mentorRepository.findById(id);
    }
}