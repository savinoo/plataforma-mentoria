// src/main/java/br/edu/iff/mentorplatform/repository/MentorRepository.java
package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByAprovadoIsTrue();
    List<Mentor> findByAprovadoIsFalse(); // Para o admin
}
