package br.edu.iff.ccc.webdev.plataformamentoria.service;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.*;
import br.edu.iff.ccc.webdev.plataformamentoria.repository.PedidoMentoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoMentoriaService {

    private static final Logger logger = LoggerFactory.getLogger(PedidoMentoriaService.class);

    @Autowired
    private PedidoMentoriaRepository pedidoMentoriaRepository;

    // ... (criarPedido e findPedidosPendentesByMentor permanecem iguais) ...
    @Transactional
    public PedidoMentoria criarPedido(Mentorado mentorado, Mentor mentor, String mensagem, boolean deRecomendacao) {
        if (pedidoMentoriaRepository.existsByMentoradoAndMentorAndStatus(mentorado, mentor, PedidoMentoriaStatus.PENDENTE)) {
            throw new IllegalStateException("Já existe um pedido pendente para este mentor.");
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
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(PedidoMentoriaStatus.ACEITO);
        pedido.setDataResposta(LocalDateTime.now());

        Mentorado mentorado = pedido.getMentorado();
        Mentor mentor = pedido.getMentor();
        
        // Cria a relação
        mentorado.getMentores().add(mentor);

        // ATIVAÇÃO DO HUB DE COMUNICAÇÃO (a ser implementado no futuro)
        logger.info("Hub de comunicação ativado entre " + mentorado.getEmail() + " e " + mentor.getEmail());
        
        // SIMULAÇÃO DE NOTIFICAÇÃO
        logger.info("NOTIFICACAO: O seu pedido de mentoria com " + mentor.getNome() + " foi aceite!");

        pedidoMentoriaRepository.save(pedido);
    }

    @Transactional
    public void recusarPedido(Long pedidoId, String motivo) {
        PedidoMentoria pedido = pedidoMentoriaRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(PedidoMentoriaStatus.RECUSADO);
        pedido.setDataResposta(LocalDateTime.now());
        pedido.setMotivoRecusa(motivo);
        
        // SIMULAÇÃO DE NOTIFICAÇÃO
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
}