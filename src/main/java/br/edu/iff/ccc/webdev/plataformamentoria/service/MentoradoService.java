// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentorService.java
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentoradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MentoradoService {

    @Autowired
    private MentoradoRepository mentoradoRepository;

    // Método para salvar (criar ou atualizar) um mentorado (POST)
    public Mentorado saveMentorado(Mentorado mentorado) {
        // Futuramente, pode incluir validações, como verificar se o email já existe
        return mentoradoRepository.save(mentorado);
    }

    // Método para buscar todos os mentorados (GET)
    public List<Mentorado> findAllMentorados() {
        return mentoradoRepository.findAll();
    }

    // Método para buscar um mentorado por ID (GET)
    public Optional<Mentorado> findMentoradoById(Long id) {
        return mentoradoRepository.findById(id);
    }
}