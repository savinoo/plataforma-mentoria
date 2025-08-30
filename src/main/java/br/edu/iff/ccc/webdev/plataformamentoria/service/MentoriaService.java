// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentoriaService.java
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MentoriaService {
    @Autowired
    private MentoriaRepository mentoriaRepository;

    public Mentoria save(Mentoria mentoria) {
        return mentoriaRepository.save(mentoria);
    }
    
    public List<Mentoria> findAll() {
        return mentoriaRepository.findAll();
    }
}