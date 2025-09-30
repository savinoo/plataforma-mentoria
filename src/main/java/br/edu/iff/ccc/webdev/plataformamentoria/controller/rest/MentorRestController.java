package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentorFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mentores")
@Tag(name = "Mentores", description = "Endpoints para gerenciamento de Mentores")
public class MentorRestController {

    @Autowired
    private MentorService mentorService;

    @Operation(summary = "Lista todos os mentores com filtros opcionais",
               description = "Retorna uma lista de mentores aprovados, permitindo filtrar por termo, especialidade e status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Mentor>> getAllMentores(
        @RequestParam(value = "termo", required = false) String termo,
        @RequestParam(value = "especialidade", required = false) String especialidade,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "sort", required = false, defaultValue = "relevancia") String sort) {
        return ResponseEntity.ok(mentorService.searchMentores(termo, especialidade, status, sort));
    }

    @Operation(summary = "Busca um mentor por ID", description = "Retorna os detalhes de um mentor específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mentor encontrado"),
        @ApiResponse(responseCode = "404", description = "Mentor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mentor> getMentorById(@PathVariable Long id) {
        Mentor mentor = mentorService.findMentorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor", "id", id));
        return ResponseEntity.ok(mentor);
    }

    @Operation(summary = "Cria um novo mentor", description = "Registra uma nova aplicação de mentor na plataforma. O perfil ficará pendente de aprovação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mentor criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<Mentor> createMentor(@Valid @RequestBody MentorFormDTO mentorDTO) {
        Mentor novoMentor = mentorService.saveMentor(mentorDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoMentor.getId()).toUri();
        return ResponseEntity.created(location).body(novoMentor);
    }

    @Operation(summary = "Atualiza o perfil de um mentor", description = "Atualiza informações detalhadas do perfil de um mentor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil do mentor atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentor não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Mentor> updateMentor(@PathVariable Long id, @Valid @RequestBody Mentor mentorDetails) {
        Mentor mentorAtualizado = mentorService.updateMentorById(id, mentorDetails);
        return ResponseEntity.ok(mentorAtualizado);
    }

    @Operation(summary = "Exclui um mentor", description = "Remove o registro de um mentor da plataforma.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Mentor excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentor não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentor(@PathVariable Long id) {
        mentorService.findMentorById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentor", "id", id));
        mentorService.deleteMentor(id);
        return ResponseEntity.noContent().build();
    }
}