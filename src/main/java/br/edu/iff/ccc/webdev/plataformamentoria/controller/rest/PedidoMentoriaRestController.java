package br.edu.iff.ccc.webdev.plataformamentoria.controller.rest;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentorado;
import br.edu.iff.ccc.webdev.plataformamentoria.entities.PedidoMentoria;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentorService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.MentoradoService;
import br.edu.iff.ccc.webdev.plataformamentoria.service.PedidoMentoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos-mentoria")
public class PedidoMentoriaRestController {

    @Autowired
    private PedidoMentoriaService pedidoMentoriaService;

    @Autowired
    private MentoradoService mentoradoService;

    @Autowired
    private MentorService mentorService;

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Map<String, Object> payload) {
        try {
            Long mentoradoId = Long.parseLong(payload.get("mentoradoId").toString());
            Long mentorId = Long.parseLong(payload.get("mentorId").toString());
            String mensagem = (String) payload.get("mensagem");
            boolean deRecomendacao = (Boolean) payload.getOrDefault("deRecomendacao", false);

            Mentorado mentorado = mentoradoService.findMentoradoById(mentoradoId)
                .orElseThrow(() -> new RuntimeException("Mentorado não encontrado"));
            Mentor mentor = mentorService.findMentorById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor não encontrado"));

            PedidoMentoria pedido = pedidoMentoriaService.criarPedido(mentorado, mentor, mensagem, deRecomendacao);
            return ResponseEntity.status(201).body(pedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/aceitar")
    public ResponseEntity<?> aceitarPedido(@PathVariable Long id) {
        try {
            pedidoMentoriaService.aceitarPedido(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/recusar")
    public ResponseEntity<?> recusarPedido(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String motivo = payload.get("motivo");
            pedidoMentoriaService.recusarPedido(id, motivo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}