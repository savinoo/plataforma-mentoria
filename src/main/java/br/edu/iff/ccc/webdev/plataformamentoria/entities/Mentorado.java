// src/main/java/br/edu/iff/ccc/webdev/plataformamentoria/entities/Mentorado.java
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
public class Mentorado extends Usuario {

    // A anotação @NotBlank foi removida para permitir que o campo seja preenchido posteriormente.
    private String interesses; // Ex: "Desenvolvimento Web, Carreira, Spring Boot"

    // Construtor padrão
    public Mentorado() {}

    // Getters e Setters
    public String getInteresses() {
        return interesses;
    }

    public void setInteresses(String interesses) {
        this.interesses = interesses;
    }

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