package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MentoradoRepository extends JpaRepository<Mentorado, Long> {
    Optional<Mentorado> findByEmail(String email);
}