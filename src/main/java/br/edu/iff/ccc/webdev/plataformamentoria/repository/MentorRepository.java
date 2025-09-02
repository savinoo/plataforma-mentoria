
package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByAprovadoIsTrue();
    List<Mentor> findByAprovadoIsFalse();
    Optional<Mentor> findByEmail(String email);

    @Query("SELECT m FROM Mentor m WHERE m.aprovado = true AND " +
           "(:termo IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(m.resumoProfissional) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(m.competencias) LIKE LOWER(CONCAT('%', :termo, '%'))) AND " +
           "(:especialidade IS NULL OR m.especialidade = :especialidade) AND " +
           "(:status IS NULL OR m.statusDisponibilidade = :status)")
    List<Mentor> findWithFilters(
        @Param("termo") String termo,
        @Param("especialidade") String especialidade,
        @Param("status") String status,
        Sort sort);

    @Query("SELECT DISTINCT m.especialidade FROM Mentor m WHERE m.aprovado = true ORDER BY m.especialidade")
    List<String> findDistinctEspecialidades();
    
    List<Mentor> findByAprovadoIsTrueAndIdNot(Long mentoradoId);
}