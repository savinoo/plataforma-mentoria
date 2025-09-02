package br.edu.iff.ccc.webdev.plataformamentoria.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MentoriaFormDTO {

    @NotBlank(message = "O tema не pode ser vazio.")
    private String tema;

    @NotNull(message = "A data da mentoria é obrigatória.")
    @Future(message = "A data da mentoria deve ser no futuro.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataHora;

    @NotNull(message = "É necessário selecionar um mentor.")
    private Long mentorId;

    @NotNull(message = "É necessário selecionar um mentorado.")
    private Long mentoradoId;

    // Getters e Setters
    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getMentorId() {
        return mentorId;
    }

    public void setMentorId(Long mentorId) {
        this.mentorId = mentorId;
    }

    public Long getMentoradoId() {
        return mentoradoId;
    }

    public void setMentoradoId(Long mentoradoId) {
        this.mentoradoId = mentoradoId;
    }
}