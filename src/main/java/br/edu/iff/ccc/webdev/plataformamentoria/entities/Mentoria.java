
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Mentoria implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data da mentoria é obrigatória.")
    @Future(message = "A data da mentoria deve ser no futuro.")
    private LocalDateTime dataHora;

    @NotBlank(message = "O tema não pode ser vazio.")
    private String tema;


    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    @NotNull(message = "É necessário selecionar um mentor.")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "mentorado_id", nullable = false)
    @NotNull(message = "É necessário selecionar um mentorado.")
    private Mentorado mentorado;

  
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }
    public Mentor getMentor() { return mentor; }
    public void setMentor(Mentor mentor) { this.mentor = mentor; }
    public Mentorado getMentorado() { return mentorado; }
    public void setMentorado(Mentorado mentorado) { this.mentorado = mentorado; }
}