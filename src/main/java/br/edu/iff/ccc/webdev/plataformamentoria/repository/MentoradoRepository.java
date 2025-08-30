package br.edu.iff.ccc.webdev.plataformamentoria.repository;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentoradoRepository extends JpaRepository<Mentorado, Long> {}