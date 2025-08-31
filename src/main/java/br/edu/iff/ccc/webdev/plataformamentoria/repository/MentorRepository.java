// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/repository/MentorRepository.java
package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByAprovadoIsTrue();
    List<Mentor> findByAprovadoIsFalse(); // Para o admin
    Optional<Mentor> findByEmail(String email);
}