package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mentores")
public class MentorRestController {

    @Autowired
    private MentorService mentorService;

    @GetMapping
    public ResponseEntity<List<Mentor>> getAllMentores(
        @RequestParam(value = "termo", required = false) String termo,
        @RequestParam(value = "especialidade", required = false) String especialidade,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "sort", required = false, defaultValue = "relevancia") String sort) {
        return ResponseEntity.ok(mentorService.searchMentores(termo, especialidade, status, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getMentorById(@PathVariable Long id) {
        Optional<Mentor> mentor = mentorService.findMentorById(id);
        return mentor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mentor> createMentor(@RequestBody MentorFormDTO mentorDTO) {
        Mentor novoMentor = mentorService.saveMentor(mentorDTO);
        return ResponseEntity.status(201).body(novoMentor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentor> updateMentor(@PathVariable Long id, @RequestBody Mentor mentorDetails) {
        Optional<Mentor> mentorOptional = mentorService.findMentorById(id);
        if (mentorOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Mentor mentor = mentorOptional.get();
        mentorService.updateProfile(mentor.getEmail(), mentorDetails);
        return ResponseEntity.ok(mentor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long id) {
        if (mentorService.findMentorById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mentorService.deleteMentor(id);
        return ResponseEntity.noContent().build();
    }
}