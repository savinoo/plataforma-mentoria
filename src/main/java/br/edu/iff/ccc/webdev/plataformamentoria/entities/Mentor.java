// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/entities/Mentor.java
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Mentor extends Usuario {

    @NotBlank(message = "A especialidade não pode ser vazia.")
    private String especialidade;

    private boolean aprovado = false; // Por padrão, um novo mentor não está aprovado

    @Lob
    @Column(length = 1500)
    private String resumoProfissional;

    @Lob
    @Column(length = 1500)
    private String filosofiaMentoria;

    @Column(length = 500)
    private String competencias; // Armazenará tags separadas por vírgula

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mentor_areas_especializacao", joinColumns = @JoinColumn(name = "mentor_id"))
    @Column(name = "area_especializacao")
    private Set<String> areasDeEspecializacao = new HashSet<>();
    
    // Construtor padrão
    public Mentor() {}

    // Getters e Setters
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public boolean isAprovado() { return aprovado; }
    public void setAprovado(boolean aprovado) { this.aprovado = aprovado; }
    public String getResumoProfissional() { return resumoProfissional; }
    public void setResumoProfissional(String resumoProfissional) { this.resumoProfissional = resumoProfissional; }
    public String getFilosofiaMentoria() { return filosofiaMentoria; }
    public void setFilosofiaMentoria(String filosofiaMentoria) { this.filosofiaMentoria = filosofiaMentoria; }
    public String getCompetencias() { return competencias; }
    public void setCompetencias(String competencias) { this.competencias = competencias; }
    public Set<String> getAreasDeEspecializacao() { return areasDeEspecializacao; }
    public void setAreasDeEspecializacao(Set<String> areasDeEspecializacao) { this.areasDeEspecializacao = areasDeEspecializacao; }

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mentoria> mentorias = new ArrayList<>();

    public List<Mentoria> getMentorias() { return mentorias; }
    public void setMentorias(List<Mentoria> mentorias) { this.mentorias = mentorias; }
}