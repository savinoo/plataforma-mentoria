package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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
        Mentorado mentorado = mentoradoService.findMentoradoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", id));
        return ResponseEntity.ok(mentorado);
    }

    @PostMapping
    public ResponseEntity<Mentorado> createMentorado(@Valid @RequestBody Mentorado mentorado) {
        Mentorado novoMentorado = mentoradoService.saveMentorado(mentorado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoMentorado.getId()).toUri();
        return ResponseEntity.created(location).body(novoMentorado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentorado> updateMentorado(@PathVariable Long id, @Valid @RequestBody Mentorado mentoradoDetails) {
        Mentorado mentoradoExistente = mentoradoService.findMentoradoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", id));
        
        // Atualizar os campos do mentorado existente
        mentoradoExistente.setNome(mentoradoDetails.getNome());
        mentoradoExistente.setEmail(mentoradoDetails.getEmail());
        if (mentoradoDetails.getSenha() != null && !mentoradoDetails.getSenha().trim().isEmpty()) {
            mentoradoExistente.setSenha(mentoradoDetails.getSenha());
        }
        
        // Salvar e retornar o mentorado atualizado
        Mentorado mentoradoAtualizado = mentoradoService.saveMentorado(mentoradoExistente);
        return ResponseEntity.ok(mentoradoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorado(@PathVariable Long id) {
        mentoradoService.findMentoradoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", id));
        mentoradoService.deleteMentorado(id);
        return ResponseEntity.noContent().build();
    }
}