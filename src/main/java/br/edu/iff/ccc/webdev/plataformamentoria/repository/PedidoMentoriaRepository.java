package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoMentoriaRepository extends JpaRepository<PedidoMentoria, Long> {
    
    List<PedidoMentoria> findByMentorAndStatus(Mentor mentor, PedidoMentoriaStatus status);

    long countByOriginadoDeRecomendacaoIsTrue();

    long countByOriginadoDeRecomendacaoIsTrueAndStatus(PedidoMentoriaStatus status);
    
    // Novo método para verificar pedido pendente
    boolean existsByMentoradoAndMentorAndStatus(Mentorado mentorado, Mentor mentor, PedidoMentoriaStatus status);
    
    // Novo método para buscar os pedidos de um mentorado
    List<PedidoMentoria> findByMentorado(Mentorado mentorado, org.springframework.data.domain.Sort sort);
}