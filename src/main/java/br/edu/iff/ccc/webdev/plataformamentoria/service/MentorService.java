// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentorService.java
package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.dto.RecomendacaoDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MentorService {

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Mentor saveMentor(MentorFormDTO mentorDTO) {
        Mentor mentor = new Mentor();
        mentor.setNome(mentorDTO.getNome() + " " + mentorDTO.getSobrenome());
        mentor.setEmail(mentorDTO.getEmail());
        mentor.setSenha(passwordEncoder.encode(mentorDTO.getSenha()));
        mentor.setEspecialidade(mentorDTO.getEspecialidade());
        mentor.addPapel("MENTOR");
        mentor.setAprovado(false);
        mentor.setStatusDisponibilidade("Disponível");
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
        
        mentor.setDisponibilidadeMensal(profileData.getDisponibilidadeMensal());
        mentor.setFormatosReuniao(profileData.getFormatosReuniao());
        mentor.setMaxMentorados(profileData.getMaxMentorados());

        if (mentor.getStatusDisponibilidade() == null) {
            mentor.setStatusDisponibilidade("Disponível");
        }
        
        mentorRepository.save(mentor);
    }
    
    public List<Mentor> searchMentores(String termo, String especialidade, String status, String sort) {
        String termoBusca = StringUtils.hasText(termo) ? termo : null;
        String especialidadeFiltro = StringUtils.hasText(especialidade) ? especialidade : null;
        String statusFiltro = StringUtils.hasText(status) ? status : null;

        Sort.Direction direction = Sort.Direction.ASC;
        String sortProperty = "nome";

        if ("avaliacao".equals(sort)) {
            // sortProperty = "avaliacaoMedia"; 
            // direction = Sort.Direction.DESC;
        } else if ("recente".equals(sort)) {
            // sortProperty = "ultimaAtividade"; 
            // direction = Sort.Direction.DESC;
        }

        return mentorRepository.findWithFilters(termoBusca, especialidadeFiltro, statusFiltro, Sort.by(direction, sortProperty));
    }

    public List<String> getEspecialidades() {
        return mentorRepository.findDistinctEspecialidades();
    }
    
    public List<RecomendacaoDTO> recomendarMentores(Mentorado mentorado) {
        if (mentorado == null || !mentorado.isOnboardingCompleto()) {
            return Collections.emptyList();
        }

        Set<String> interessesMentorado = parseToSet(mentorado.getAreasDeInteresse());
        Set<String> competenciasMentorado = parseToSet(mentorado.getCompetenciasDesejadas());

        if (interessesMentorado.isEmpty() && competenciasMentorado.isEmpty()) {
            return Collections.emptyList();
        }

        List<Mentor> todosMentores = mentorRepository.findByAprovadoIsTrueAndIdNot(mentorado.getId());

        return todosMentores.stream()
            .map(mentor -> {
                Set<String> areasMentor = mentor.getAreasDeEspecializacao();
                Set<String> competenciasMentor = parseToSet(mentor.getCompetencias());
                
                Set<String> interessesEmComum = new HashSet<>(interessesMentorado);
                interessesEmComum.retainAll(areasMentor);

                Set<String> competenciasEmComum = new HashSet<>(competenciasMentorado);
                competenciasEmComum.retainAll(competenciasMentor);

                int score = interessesEmComum.size() + competenciasEmComum.size();

                if (score > 0) {
                    String justificativa = "Recomendado por afinidade em suas áreas de interesse e competências.";
                    if (!interessesEmComum.isEmpty()) {
                        justificativa = "Recomendado pelo interesse em comum em '" + interessesEmComum.iterator().next() + "'.";
                    } else if (!competenciasEmComum.isEmpty()) {
                        justificativa = "Recomendado pela sua competência em '" + competenciasEmComum.iterator().next() + "'.";
                    }
                    return new AbstractMap.SimpleEntry<>(new RecomendacaoDTO(mentor, justificativa), score);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .sorted(Map.Entry.<RecomendacaoDTO, Integer>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private Set<String> parseToSet(String s) {
        if (!StringUtils.hasText(s)) return Collections.emptySet();
        return Stream.of(s.split(","))
                     .map(String::trim)
                     .filter(StringUtils::hasText)
                     .collect(Collectors.toSet());
    }
}