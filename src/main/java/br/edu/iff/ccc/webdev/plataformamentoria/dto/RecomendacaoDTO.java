package br.edu.iff.ccc.webdev.plataformamentoria.dto;

import br.edu.iff.ccc.webdev.plataformamentoria.entities.Mentor;

public class RecomendacaoDTO {

    private Mentor mentor;
    private String justificativa;

    public RecomendacaoDTO(Mentor mentor, String justificativa) {
        this.mentor = mentor;
        this.justificativa = justificativa;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}