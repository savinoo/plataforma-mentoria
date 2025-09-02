package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoriaRepository extends JpaRepository<Mentoria, Long> {}