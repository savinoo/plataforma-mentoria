// src/main/java/br/edu/iff/mentorplatform/entities/Mentor.java
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import jakarta.persistence.CascadeType;

@Entity
public class Mentor extends Usuario {

    @NotBlank(message = "A especialidade não pode ser vazia.")
    private String especialidade;

    private boolean aprovado = false; // Por padrão, um novo mentor não está aprovado

    
    // Construtor padrão
    public Mentor() {}

    // Getters e Setters
    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mentoria> mentorias = new ArrayList<>();

    // Getter e Setter para a lista
    public List<Mentoria> getMentorias() {
    return mentorias;
}

    public void setMentorias(List<Mentoria> mentorias) {
        this.mentorias = mentorias;
    }

     public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }
    
}



