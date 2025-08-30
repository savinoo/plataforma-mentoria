// src/main/java/br/edu/iff/mentorplatform/repository/MentorRepository.java
package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
}
