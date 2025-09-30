package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.PedidoMentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.PedidoMentoriaStatus;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.PedidoMentoriaService;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pedidos-mentoria")
@Tag(name = "Pedidos de Mentoria", description = "API para gerenciamento de pedidos de mentoria")
public class PedidoMentoriaRestController {

    @Autowired
    private PedidoMentoriaService pedidoMentoriaService;

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private MentorService mentorService;

    @GetMapping
    @Operation(summary = "Listar todos os pedidos de mentoria", description = "Retorna uma lista com todos os pedidos de mentoria cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public ResponseEntity<List<PedidoMentoria>> listarTodos() {
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido de mentoria específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<PedidoMentoria> buscarPorId(
        @Parameter(description = "ID do pedido de mentoria", required = true) @PathVariable Long id) {
        Optional<PedidoMentoria> pedido = pedidoMentoriaService.buscarPorId(id);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            throw new ResourceNotFoundException("Pedido de mentoria", "id", id);
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pedidos por status", description = "Retorna uma lista de pedidos filtrados por status (PENDENTE, ACEITO, RECUSADO)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    })
    public ResponseEntity<List<PedidoMentoria>> listarPorStatus(
        @Parameter(description = "Status do pedido (PENDENTE, ACEITO, RECUSADO)", required = true) @PathVariable String status) {
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarPorStatus(status.toUpperCase());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mentor/{mentorId}")
    @Operation(summary = "Listar pedidos por mentor", description = "Retorna uma lista de pedidos dirigidos a um mentor específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentor não encontrado")
    })
    public ResponseEntity<List<PedidoMentoria>> listarPorMentor(
        @Parameter(description = "ID do mentor", required = true) @PathVariable Long mentorId) {
        // Verificar se o mentor existe
        mentorService.findMentorById(mentorId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor", "id", mentorId));
        
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarPorMentor(mentorId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mentorado/{mentoradoId}")
    @Operation(summary = "Listar pedidos por mentorado", description = "Retorna uma lista de pedidos criados por um mentorado específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Mentorado não encontrado")
    })
    public ResponseEntity<List<PedidoMentoria>> listarPorMentorado(
        @Parameter(description = "ID do mentorado", required = true) @PathVariable Long mentoradoId) {
        // Verificar se o mentorado existe
        mentoradoService.findMentoradoById(mentoradoId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", mentoradoId));
        
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarPorMentorado(mentoradoId);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    @Operation(summary = "Criar novo pedido de mentoria", description = "Cria um novo pedido de mentoria com os dados fornecidos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Mentor ou Mentorado não encontrado")
    })
    public ResponseEntity<PedidoMentoria> criarPedido(
        @Parameter(description = "Dados do pedido (mentoradoId, mentorId, mensagem, deRecomendacao)", required = true) 
        @RequestBody Map<String, Object> payload) {
        Long mentoradoId = Long.parseLong(payload.get("mentoradoId").toString());
        Long mentorId = Long.parseLong(payload.get("mentorId").toString());
        String mensagem = (String) payload.get("mensagem");
        boolean deRecomendacao = (Boolean) payload.getOrDefault("deRecomendacao", false);

        Mentorado mentorado = mentoradoService.findMentoradoById(mentoradoId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", mentoradoId));
        Mentor mentor = mentorService.findMentorById(mentorId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor", "id", mentorId));

        PedidoMentoria pedido = pedidoMentoriaService.criarPedido(mentorado, mentor, mensagem, deRecomendacao);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(pedido);
    }

    @PatchMapping("/{id}/aceitar")
    @Operation(summary = "Aceitar pedido de mentoria", description = "Aceita um pedido de mentoria e cria automaticamente uma mentoria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido aceito com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser aceito (já processado)")
    })
    public ResponseEntity<Void> aceitarPedido(
        @Parameter(description = "ID do pedido de mentoria", required = true) @PathVariable Long id) {
        pedidoMentoriaService.aceitarPedido(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/recusar")
    @Operation(summary = "Recusar pedido de mentoria", description = "Recusa um pedido de mentoria com motivo opcional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido recusado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser recusado (já processado)")
    })
    public ResponseEntity<Void> recusarPedido(
        @Parameter(description = "ID do pedido de mentoria", required = true) @PathVariable Long id, 
        @Parameter(description = "Dados com motivo da recusa", required = false) @RequestBody Map<String, String> payload) {
        String motivo = payload.get("motivo");
        pedidoMentoriaService.recusarPedido(id, motivo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pedido de mentoria", description = "Atualiza dados de um pedido pendente (apenas mensagem e tipo)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser atualizado (não está pendente) ou dados inválidos")
    })
    public ResponseEntity<PedidoMentoria> atualizar(
        @Parameter(description = "ID do pedido de mentoria", required = true) @PathVariable Long id, 
        @Parameter(description = "Dados para atualização (mensagem, deRecomendacao)", required = true) @Valid @RequestBody Map<String, Object> payload) {
        Optional<PedidoMentoria> pedidoExistente = pedidoMentoriaService.buscarPorId(id);
        if (!pedidoExistente.isPresent()) {
            throw new ResourceNotFoundException("Pedido de mentoria", "id", id);
        }

        PedidoMentoria pedido = pedidoExistente.get();
        
        // Só permite atualizar se estiver pendente
        if (!PedidoMentoriaStatus.PENDENTE.equals(pedido.getStatus())) {
            throw new RegraDeNegocioException("Apenas pedidos pendentes podem ser atualizados");
        }

        // Atualizar campos permitidos
        if (payload.containsKey("mensagem")) {
            String novaMensagem = (String) payload.get("mensagem");
            if (novaMensagem == null || novaMensagem.trim().isEmpty()) {
                throw new RegraDeNegocioException("A mensagem não pode ser vazia");
            }
            pedido.setMensagem(novaMensagem);
        }

        if (payload.containsKey("deRecomendacao")) {
            Boolean deRecomendacao = (Boolean) payload.get("deRecomendacao");
            if (deRecomendacao != null) {
                pedido.setOriginadoDeRecomendacao(deRecomendacao);
            }
        }
        
        PedidoMentoria pedidoAtualizado = pedidoMentoriaService.salvar(pedido);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar pedido de mentoria", description = "Cancela/deleta um pedido de mentoria pendente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado (não está pendente)")
    })
    public ResponseEntity<Void> cancelar(
        @Parameter(description = "ID do pedido de mentoria", required = true) @PathVariable Long id) {
        Optional<PedidoMentoria> pedidoExistente = pedidoMentoriaService.buscarPorId(id);
        if (!pedidoExistente.isPresent()) {
            throw new ResourceNotFoundException("Pedido de mentoria", "id", id);
        }

        PedidoMentoria pedido = pedidoExistente.get();
        
        // Só permite cancelar se estiver pendente
        if (!PedidoMentoriaStatus.PENDENTE.equals(pedido.getStatus())) {
            throw new RegraDeNegocioException("Apenas pedidos pendentes podem ser cancelados");
        }

        pedidoMentoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}