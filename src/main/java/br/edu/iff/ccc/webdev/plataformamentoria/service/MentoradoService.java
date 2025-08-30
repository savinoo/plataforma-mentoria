// CORREÇÃO PARA:
// savinoo/plataforma-mentoria/plataforma-mentoria-BranchTestes/src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/service/MentoradoService.java

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

    /**
     * Salva (cria ou atualiza) um mentorado no banco de dados.
     * Este é o método que será chamado pelo AuthController.
     * @param mentorado A entidade Mentorado a ser salva.
     * @return A entidade Mentorado salva.
     */
    public Mentorado saveMentorado(Mentorado mentorado) {
        // Validações futuras, como verificar se o e-mail já existe, podem ser adicionadas aqui.
        return mentoradoRepository.save(mentorado);
    }

    /**
     * Busca todos os mentorados cadastrados.
     * @return Uma lista de todos os mentorados.
     */
    public List<Mentorado> findAllMentorados() {
        return mentoradoRepository.findAll();
    }

    /**
     * Busca um mentorado específico pelo seu ID.
     * @param id O ID do mentorado a ser buscado.
     * @return Um Optional contendo o mentorado, se encontrado.
     */
    public Optional<Mentorado> findMentoradoById(Long id) {
        return mentoradoRepository.findById(id);
    }
}