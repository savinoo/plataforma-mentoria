
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Mentorado extends Usuario {

    // ... (campos existentes) ...
    @Column(nullable = false)
    private String interesses = "N/D";

    @Column(name = "areasDeInteresse")
    private String areasDeInteresse; 
    @Column(name = "disciplinasDeMaiorDificuldade")
    private String disciplinasDeMaiorDificuldade;
    @Column(name = "conquistasAcademicas")
    private String conquistasAcademicas;
    @Column(name = "industriasDeInteresse")
    private String industriasDeInteresse;
    @Column(name = "caminhosDeCarreira")
    private String caminhosDeCarreira;
    @Column(name = "empresasDeInteresse")
    private String empresasDeInteresse;
    @Column(name = "competenciasDesejadas")
    private String competenciasDesejadas;
    @Column(name = "objetivosMentoria")
    private String objetivosMentoria;

    @Column(columnDefinition = "boolean default false")
    private boolean onboardingCompleto = false;

    @Column(columnDefinition = "boolean default false")
    private boolean visibilidadeInfoAcademica = false;
    @Column(columnDefinition = "boolean default false")
    private boolean visibilidadeAspiracoesCarreira = false;
    @Column(columnDefinition = "boolean default false")
    private boolean visibilidadeCompetencias = false;


    @OneToMany(mappedBy = "mentorado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mentoria> mentorias = new ArrayList<>();

 
    @ManyToMany
    @JoinTable(
        name = "mentoria_relacao",
        joinColumns = @JoinColumn(name = "mentorado_id"),
        inverseJoinColumns = @JoinColumn(name = "mentor_id")
    )
    private Set<Mentor> mentores = new HashSet<>();

    public Mentorado() {}

 
    public String getInteresses() { return interesses; }
    public void setInteresses(String interesses) { this.interesses = interesses; }
    public String getAreasDeInteresse() { return areasDeInteresse; }
    public void setAreasDeInteresse(String areasDeInteresse) { this.areasDeInteresse = areasDeInteresse; }
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
    public boolean isVisibilidadeInfoAcademica() { return visibilidadeInfoAcademica; }
    public void setVisibilidadeInfoAcademica(boolean visibilidadeInfoAcademica) { this.visibilidadeInfoAcademica = visibilidadeInfoAcademica; }
    public boolean isVisibilidadeAspiracoesCarreira() { return visibilidadeAspiracoesCarreira; }
    public void setVisibilidadeAspiracoesCarreira(boolean visibilidadeAspiracoesCarreira) { this.visibilidadeAspiracoesCarreira = visibilidadeAspiracoesCarreira; }
    public boolean isVisibilidadeCompetencias() { return visibilidadeCompetencias; }
    public void setVisibilidadeCompetencias(boolean visibilidadeCompetencias) { this.visibilidadeCompetencias = visibilidadeCompetencias; }
    public List<Mentoria> getMentorias() { return mentorias; }
    public void setMentorias(List<Mentoria> mentorias) { this.mentorias = mentorias; }
    public Set<Mentor> getMentores() { return mentores; } 
    public void setMentores(Set<Mentor> mentores) { this.mentores = mentores; } 
}
