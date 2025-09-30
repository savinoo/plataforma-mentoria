package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.*;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.RecursoNaoEncontradoException;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.PedidoMentoriaRepository;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoMentoriaService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoMentoriaService.class);

    @Autowired
    private PedidoMentoriaRepository pedidoMentoriaRepository;
    
    @Autowired
    private MentoriaService mentoriaService;
    @Transactional
    public PedidoMentoria criarPedido(Mentorado mentorado, Mentor mentor, String mensagem, boolean deRecomendacao) {
        if (pedidoMentoriaRepository.existsByMentoradoAndMentorAndStatus(mentorado, mentor, PedidoMentoriaStatus.PENDENTE)) {
            throw new RegraDeNegocioException("Já existe um pedido pendente para este mentor.");
        }

        PedidoMentoria pedido = new PedidoMentoria();
        pedido.setMentorado(mentorado);
        pedido.setMentor(mentor);
        pedido.setMensagem(mensagem);
        pedido.setOriginadoDeRecomendacao(deRecomendacao);
        pedido.setStatus(PedidoMentoriaStatus.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        return pedidoMentoriaRepository.save(pedido);
    }

    public List<PedidoMentoria> findPedidosPendentesByMentor(Mentor mentor) {
        return pedidoMentoriaRepository.findByMentorAndStatus(mentor, PedidoMentoriaStatus.PENDENTE);
    }
    
    @Transactional
    public void aceitarPedido(Long pedidoId) {
        PedidoMentoria pedido = pedidoMentoriaRepository.findById(pedidoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido de mentoria não encontrado com o ID: " + pedidoId));
        pedido.setStatus(PedidoMentoriaStatus.ACEITO);
        pedido.setDataResposta(LocalDateTime.now());

        Mentorado mentorado = pedido.getMentorado();
        Mentor mentor = pedido.getMentor();

        // Adicionar mentor aos mentores do mentorado (relacionamento)
        mentorado.getMentores().add(mentor);

        try {
            Mentoria novaMentoria = new Mentoria();
            novaMentoria.setMentor(mentor);
            novaMentoria.setMentorado(mentorado);
            novaMentoria.setDataHora(LocalDateTime.now().plusDays(7)); // Agendar para próxima semana
            novaMentoria.setTema("Mentoria iniciada: " + pedido.getMensagem().substring(0, Math.min(pedido.getMensagem().length(), 50)) + "...");
            
            // Salvar a mentoria
            mentoriaService.save(novaMentoria);
            logger.info("✅ Mentoria criada automaticamente - ID: {} - Mentor: {} - Mentorado: {}", 
                       novaMentoria.getId(), mentor.getNome(), mentorado.getNome());
            
        } catch (Exception e) {
            logger.warn("⚠️ Erro ao criar mentoria automaticamente: {}", e.getMessage());
            // Não quebrar o fluxo se der erro na criação da mentoria
        }

        logger.info("Hub de comunicação ativado entre " + mentorado.getEmail() + " e " + mentor.getEmail());
        logger.info("NOTIFICACAO: O seu pedido de mentoria com " + mentor.getNome() + " foi aceito!");

        pedidoMentoriaRepository.save(pedido);
    }

    @Transactional
    public void recusarPedido(Long pedidoId, String motivo) {
        PedidoMentoria pedido = pedidoMentoriaRepository.findById(pedidoId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido de mentoria não encontrado com o ID: " + pedidoId));
        pedido.setStatus(PedidoMentoriaStatus.RECUSADO);
        pedido.setDataResposta(LocalDateTime.now());
        pedido.setMotivoRecusa(motivo);

        logger.info("NOTIFICACAO: O seu pedido de mentoria com " + pedido.getMentor().getNome() + " foi recusado. Motivo: " + motivo);
        
        pedidoMentoriaRepository.save(pedido);
    }
    
    public List<PedidoMentoria> findPedidosByMentorado(Mentorado mentorado) {
        return pedidoMentoriaRepository.findByMentorado(mentorado, Sort.by(Sort.Direction.DESC, "dataPedido"));
    }

    public long getTotalPedidosDeRecomendacoes() {
        return pedidoMentoriaRepository.countByOriginadoDeRecomendacaoIsTrue();
    }

    public long getTotalPedidosAceitosDeRecomendacoes() {
        return pedidoMentoriaRepository.countByOriginadoDeRecomendacaoIsTrueAndStatus(PedidoMentoriaStatus.ACEITO);
    }

    // Novos métodos para completar CRUD
    public List<PedidoMentoria> listarTodos() {
        return pedidoMentoriaRepository.findAll(Sort.by(Sort.Direction.DESC, "dataPedido"));
    }

    public Optional<PedidoMentoria> buscarPorId(Long id) {
        return pedidoMentoriaRepository.findById(id);
    }

    public List<PedidoMentoria> listarPorStatus(String status) {
        try {
            PedidoMentoriaStatus statusEnum = PedidoMentoriaStatus.valueOf(status.toUpperCase());
            return pedidoMentoriaRepository.findByStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            logger.warn("Status inválido fornecido: {}", status);
            return List.of(); // Retorna lista vazia para status inválido
        }
    }

    public List<PedidoMentoria> listarPorMentor(Long mentorId) {
        return pedidoMentoriaRepository.findByMentorId(mentorId);
    }

    public List<PedidoMentoria> listarPorMentorado(Long mentoradoId) {
        return pedidoMentoriaRepository.findByMentoradoId(mentoradoId);
    }

    public PedidoMentoria salvar(PedidoMentoria pedido) {
        return pedidoMentoriaRepository.save(pedido);
    }

    @Transactional
    public void deletar(Long id) {
        Optional<PedidoMentoria> pedidoOpt = pedidoMentoriaRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            PedidoMentoria pedido = pedidoOpt.get();
            logger.info("Deletando pedido de mentoria - ID: {} - Status: {}", id, pedido.getStatus());
            pedidoMentoriaRepository.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Pedido de mentoria não encontrado com o ID: " + id);
        }
    }
}