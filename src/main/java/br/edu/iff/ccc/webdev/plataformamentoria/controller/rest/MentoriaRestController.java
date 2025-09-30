package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.dto.MentoriaFormDTO;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/mentorias")
@Tag(name = "Mentorias", description = "API para gerenciamento de sessões de mentoria")
public class MentoriaRestController {

    @Autowired
    private MentoriaService mentoriaService;

    @Operation(summary = "Listar todas as mentorias", description = "Retorna uma lista com todas as sessões de mentoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mentorias retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<Mentoria>> getAllMentorias() {
        return ResponseEntity.ok(mentoriaService.findAll());
    }

    @Operation(summary = "Buscar mentoria por ID", description = "Retorna uma sessão de mentoria específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mentoria encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentoria não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mentoria> getMentoriaById(
            @Parameter(description = "ID da mentoria", required = true) @PathVariable Long id) {
        Mentoria mentoria = mentoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria", "id", id));
        return ResponseEntity.ok(mentoria);
    }

    @Operation(summary = "Criar nova mentoria", description = "Cria uma nova sessão de mentoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mentoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mentor ou mentorado não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Mentoria> createMentoria(
            @Parameter(description = "Dados da mentoria a ser criada", required = true) 
            @RequestBody MentoriaFormDTO mentoriaDTO) {
        Mentoria novaMentoria = mentoriaService.save(mentoriaDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novaMentoria.getId()).toUri();
        return ResponseEntity.created(location).body(novaMentoria);
    }

    @Operation(summary = "Atualizar mentoria", description = "Atualiza os dados de uma sessão de mentoria existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mentoria atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mentoria não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Mentoria> updateMentoria(
            @Parameter(description = "ID da mentoria", required = true) @PathVariable Long id, 
            @Parameter(description = "Dados atualizados da mentoria", required = true) 
            @RequestBody MentoriaFormDTO mentoriaDTO) {
        // Verifica se a mentoria existe antes de atualizar
        mentoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria", "id", id));
        
        Mentoria mentoriaAtualizada = mentoriaService.update(id, mentoriaDTO);
        return ResponseEntity.ok(mentoriaAtualizada);
    }

    @Operation(summary = "Deletar mentoria", description = "Remove uma sessão de mentoria do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Mentoria deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentoria não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentoria(
            @Parameter(description = "ID da mentoria", required = true) @PathVariable Long id) {
        mentoriaService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentoria", "id", id));
        
        mentoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}