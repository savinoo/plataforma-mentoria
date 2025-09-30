package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentoriaFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentoria;
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
        Optional<Mentoria> mentoria = mentoriaService.findById(id);
        return mentoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
        try {
            Mentoria mentoriaAtualizada = mentoriaService.update(id, mentoriaDTO);
            return ResponseEntity.ok(mentoriaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentoria(@PathVariable Long id) {
        if (mentoriaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mentoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}