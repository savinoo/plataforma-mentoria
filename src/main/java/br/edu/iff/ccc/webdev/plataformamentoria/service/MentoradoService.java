// CORREÇÃO PARA:
// savinoo/plataforma-mentoria/plataforma-mentoria-BranchTestes/src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentoradoService.java

package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentoradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MentoradoService {

    @Autowired
    private MentoradoRepository mentoradoRepository;

    public Mentorado saveMentorado(Mentorado mentorado) {
        return mentoradoRepository.save(mentorado);
    }

    public List<Mentorado> findAllMentorados() {
        return mentoradoRepository.findAll();
    }

    public Optional<Mentorado> findMentoradoById(Long id) {
        return mentoradoRepository.findById(id);
    }

    public Optional<Mentorado> findByEmail(String email) {
        return mentoradoRepository.findByEmail(email);
    }
    
    @Transactional
    public void updateOnboarding(Mentorado mentoradoAtualizado) {
        Optional<Mentorado> mentoradoOpt = mentoradoRepository.findById(mentoradoAtualizado.getId());
        if (mentoradoOpt.isPresent()) {
            Mentorado mentorado = mentoradoOpt.get();
            mentorado.setAreasDeInteresse(mentoradoAtualizado.getAreasDeInteresse());
            mentorado.setDisciplinasDeMaiorDificuldade(mentoradoAtualizado.getDisciplinasDeMaiorDificuldade());
            mentorado.setConquistasAcademicas(mentoradoAtualizado.getConquistasAcademicas());
            mentorado.setIndustriasDeInteresse(mentoradoAtualizado.getIndustriasDeInteresse());
            mentorado.setCaminhosDeCarreira(mentoradoAtualizado.getCaminhosDeCarreira());
            mentorado.setEmpresasDeInteresse(mentoradoAtualizado.getEmpresasDeInteresse());
            mentorado.setCompetenciasDesejadas(mentoradoAtualizado.getCompetenciasDesejadas());
            mentorado.setObjetivosMentoria(mentoradoAtualizado.getObjetivosMentoria());
            mentorado.setOnboardingCompleto(true);
            mentoradoRepository.save(mentorado);
        }
    }
}