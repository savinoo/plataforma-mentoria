// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/entities/Mentorado.java
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

@Entity
public class Mentorado extends Usuario {

    private String areasDeInteresse; 
    private String disciplinasDeMaiorDificuldade;
    private String conquistasAcademicas;
    private String industriasDeInteresse;
    private String caminhosDeCarreira;
    private String empresasDeInteresse;
    private String competenciasDesejadas;
    private String objetivosMentoria;

    @Column(columnDefinition = "boolean default false")
    private boolean onboardingCompleto = false;

    // Construtor padrão
    public Mentorado() {}

    // Getters e Setters
    public String getAreasDeInteresse() {
        return areasDeInteresse;
    }

    public void setAreasDeInteresse(String areasDeInteresse) {
        this.areasDeInteresse = areasDeInteresse;
    }

    public String getDisciplinasDeMaiorDificuldade() { return disciplinasDeMaiorDificuldade; }
    public void setDisciplinasDeMaiorDificuldade(String disciplinasDeMaiorDificuldade) { this.disciplinasDeMaiorDificuldade = disciplinasDeMaiorDificuldade; }
    public String getConquistasAcademicas() { return conquistasAcademicas; }
    public void setConquistasAcademicas(String conquistasAcademicas) { this.conquistasAcademicas = conquistasAcademicas; }
    public String getIndustriasDeInteresse() { return industriasDeInteresse; }
    public void setIndustriasDeInteresse(String industriasDeInteresse) { this.industriasDeInteresse = industriasDeInteresse; }
    public String getCaminhosDeCarreira() { return caminhosDeCarreira; }
    public void setCaminhosDeCarreira(String caminhosDeCarreira) { this.caminhosDeCarreira = caminhosDeCarreira; }
    public String getEmpresasDeInteresse() { return empresasDeInteresse; }
    public void setEmpresasDeInteresse(String empresasDeInteresse) { this.empresasDeInteresse = empresasDeInteresse; }
    public String getCompetenciasDesejadas() { return competenciasDesejadas; }
    public void setCompetenciasDesejadas(String competenciasDesejadas) { this.competenciasDesejadas = competenciasDesejadas; }
    public String getObjetivosMentoria() { return objetivosMentoria; }
    public void setObjetivosMentoria(String objetivosMentoria) { this.objetivosMentoria = objetivosMentoria; }
    public boolean isOnboardingCompleto() { return onboardingCompleto; }
    public void setOnboardingCompleto(boolean onboardingCompleto) { this.onboardingCompleto = onboardingCompleto; }

    @OneToMany(mappedBy = "mentorado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mentoria> mentorias = new ArrayList<>();

    // Getter e Setter para a lista
    public List<Mentoria> getMentorias() {
    return mentorias;
}

    public void setMentorias(List<Mentoria> mentorias) {
        this.mentorias = mentorias;
    }
}