package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;


import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mentorados")
public class MentoradoRestController {

    @Autowired
    private MentoradoService mentoradoService;

    @GetMapping
    public ResponseEntity<List<Mentorado>> getAllMentorados() {
        return ResponseEntity.ok(mentoradoService.findAllMentorados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentorado> getMentoradoById(@PathVariable Long id) {
        Optional<Mentorado> mentorado = mentoradoService.findMentoradoById(id);
        return mentorado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mentorado> createMentorado(@RequestBody Mentorado mentorado) {
        Mentorado novoMentorado = mentoradoService.saveMentorado(mentorado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoMentorado.getId()).toUri();
        return ResponseEntity.created(location).body(novoMentorado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentorado> updateMentorado(@PathVariable Long id, @RequestBody Mentorado mentoradoDetails) {
        Optional<Mentorado> mentoradoOptional = mentoradoService.findMentoradoById(id);
        if (mentoradoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mentoradoDetails.setId(id);
        mentoradoService.updateOnboarding(mentoradoDetails);
        return ResponseEntity.ok(mentoradoDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorado(@PathVariable Long id) {
        if (mentoradoService.findMentoradoById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        mentoradoService.deleteMentorado(id);
        return ResponseEntity.noContent().build();
    }
}