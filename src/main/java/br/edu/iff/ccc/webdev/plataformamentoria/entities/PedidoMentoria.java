package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class PedidoMentoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorado_id", nullable = false)
    private Mentorado mentorado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @Column(nullable = false, length = 1000)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PedidoMentoriaStatus status;

    @Column(nullable = false)
    private boolean originadoDeRecomendacao = false;

    private LocalDateTime dataPedido;
    private LocalDateTime dataResposta;
    
    private String motivoRecusa; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Mentorado getMentorado() { return mentorado; }
    public void setMentorado(Mentorado mentorado) { this.mentorado = mentorado; }
    public Mentor getMentor() { return mentor; }
    public void setMentor(Mentor mentor) { this.mentor = mentor; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public PedidoMentoriaStatus getStatus() { return status; }
    public void setStatus(PedidoMentoriaStatus status) { this.status = status; }
    public boolean isOriginadoDeRecomendacao() { return originadoDeRecomendacao; }
    public void setOriginadoDeRecomendacao(boolean originadoDeRecomendacao) { this.originadoDeRecomendacao = originadoDeRecomendacao; }
    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }
    public LocalDateTime getDataResposta() { return dataResposta; }
    public void setDataResposta(LocalDateTime dataResposta) { this.dataResposta = dataResposta; }
    public String getMotivoRecusa() { return motivoRecusa; } // NOVO GETTER
    public void setMotivoRecusa(String motivoRecusa) { this.motivoRecusa = motivoRecusa; } // NOVO SETTER
}