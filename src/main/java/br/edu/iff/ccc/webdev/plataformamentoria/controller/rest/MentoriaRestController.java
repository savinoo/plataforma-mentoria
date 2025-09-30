package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentoriaFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mentorias")
public class MentoriaRestController {

    @Autowired
    private MentoriaService mentoriaService;

    @GetMapping
    public ResponseEntity<List<Mentoria>> getAllMentorias() {
        return ResponseEntity.ok(mentoriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentoria> getMentoriaById(@PathVariable Long id) {
        Mentoria mentoria = mentoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria", "id", id));
        return ResponseEntity.ok(mentoria);
    }

    @PostMapping
    public ResponseEntity<Mentoria> createMentoria(@RequestBody MentoriaFormDTO mentoriaDTO) {
        Mentoria novaMentoria = mentoriaService.save(mentoriaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaMentoria.getId()).toUri();
        return ResponseEntity.created(location).body(novaMentoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentoria> updateMentoria(@PathVariable Long id, @RequestBody MentoriaFormDTO mentoriaDTO) {
        // Verifica se a mentoria existe antes de atualizar
        mentoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria", "id", id));
        
        Mentoria mentoriaAtualizada = mentoriaService.update(id, mentoriaDTO);
        return ResponseEntity.ok(mentoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentoria(@PathVariable Long id) {
        mentoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria", "id", id));
        
        mentoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}