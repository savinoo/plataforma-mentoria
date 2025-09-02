
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentoriaFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentoradoRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MentoriaService {
    @Autowired
    private MentoriaRepository mentoriaRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private MentoradoRepository mentoradoRepository;


    public Mentoria save(Mentoria mentoria) {
        return mentoriaRepository.save(mentoria);
    }
    
    public Mentoria save(MentoriaFormDTO mentoriaDTO) {
        Mentor mentor = mentorRepository.findById(mentoriaDTO.getMentorId())
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));
        Mentorado mentorado = mentoradoRepository.findById(mentoriaDTO.getMentoradoId())
                .orElseThrow(() -> new RuntimeException("Mentorado não encontrado"));

        Mentoria mentoria = new Mentoria();
        mentoria.setTema(mentoriaDTO.getTema());
        mentoria.setDataHora(mentoriaDTO.getDataHora());
        mentoria.setMentor(mentor);
        mentoria.setMentorado(mentorado);

        return mentoriaRepository.save(mentoria);
    }
    
    public List<Mentoria> findAll() {
        return mentoriaRepository.findAll();
    }
}