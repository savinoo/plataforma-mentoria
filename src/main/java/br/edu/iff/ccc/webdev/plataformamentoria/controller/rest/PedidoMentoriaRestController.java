package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.PedidoMentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.PedidoMentoriaStatus;
import br.edu.iff.ccc.webdev.plataformamentoria.exception.ResourceNotFoundException;
import br.edu.iff.ccc.webdev.plataformamentoria.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.PedidoMentoriaService;
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
public class PedidoMentoriaRestController {

    @Autowired
    private PedidoMentoriaService pedidoMentoriaService;

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private MentorService mentorService;

    @GetMapping
    public ResponseEntity<List<PedidoMentoria>> listarTodos() {
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarTodos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoMentoria> buscarPorId(@PathVariable Long id) {
        Optional<PedidoMentoria> pedido = pedidoMentoriaService.buscarPorId(id);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            throw new ResourceNotFoundException("Pedido de mentoria", "id", id);
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PedidoMentoria>> listarPorStatus(@PathVariable String status) {
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarPorStatus(status.toUpperCase());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mentor/{mentorId}")
    public ResponseEntity<List<PedidoMentoria>> listarPorMentor(@PathVariable Long mentorId) {
        // Verificar se o mentor existe
        mentorService.findMentorById(mentorId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentor", "id", mentorId));
        
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarPorMentor(mentorId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/mentorado/{mentoradoId}")
    public ResponseEntity<List<PedidoMentoria>> listarPorMentorado(@PathVariable Long mentoradoId) {
        // Verificar se o mentorado existe
        mentoradoService.findMentoradoById(mentoradoId)
            .orElseThrow(() -> new ResourceNotFoundException("Mentorado", "id", mentoradoId));
        
        List<PedidoMentoria> pedidos = pedidoMentoriaService.listarPorMentorado(mentoradoId);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    public ResponseEntity<PedidoMentoria> criarPedido(@RequestBody Map<String, Object> payload) {
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
    public ResponseEntity<Void> aceitarPedido(@PathVariable Long id) {
        pedidoMentoriaService.aceitarPedido(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<Void> recusarPedido(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String motivo = payload.get("motivo");
        pedidoMentoriaService.recusarPedido(id, motivo);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoMentoria> atualizar(@PathVariable Long id, @Valid @RequestBody Map<String, Object> payload) {
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
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
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