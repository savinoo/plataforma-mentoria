package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/v1/mentorados")
@Tag(name = "Mentorados", description = "API para gerenciamento de mentorados")
public class MentoradoRestController {

    @Autowired
    private MentoradoService mentoradoService;

    @Operation(summary = "Listar todos os mentorados", description = "Retorna uma lista com todos os mentorados cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de mentorados retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<Mentorado>> getAllMentorados() {
        return ResponseEntity.ok(mentoradoService.findAllMentorados());
    }

    @Operation(summary = "Buscar mentorado por ID", description = "Retorna um mentorado específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mentorado encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentorado não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Mentorado> getMentoradoById(
            @Parameter(description = "ID do mentorado", required = true) @PathVariable Long id) {
        Mentorado mentorado = mentoradoService.findMentoradoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", id));
        return ResponseEntity.ok(mentorado);
    }

    @Operation(summary = "Criar novo mentorado", description = "Cadastra um novo mentorado no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Mentorado criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Mentorado> createMentorado(
            @Parameter(description = "Dados do mentorado a ser criado", required = true) 
            @Valid @RequestBody Mentorado mentorado) {
        Mentorado novoMentorado = mentoradoService.saveMentorado(mentorado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoMentorado.getId()).toUri();
        return ResponseEntity.created(location).body(novoMentorado);
    }

    @Operation(summary = "Atualizar mentorado", description = "Atualiza os dados de um mentorado existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mentorado atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Mentorado não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Mentorado> updateMentorado(
            @Parameter(description = "ID do mentorado", required = true) @PathVariable Long id, 
            @Parameter(description = "Dados atualizados do mentorado", required = true) 
            @Valid @RequestBody Mentorado mentoradoDetails) {
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

    @Operation(summary = "Deletar mentorado", description = "Remove um mentorado do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Mentorado deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentorado não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorado(
            @Parameter(description = "ID do mentorado", required = true) @PathVariable Long id) {
        mentoradoService.findMentoradoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", id));
        mentoradoService.deleteMentorado(id);
        return ResponseEntity.noContent().build();
    }
}