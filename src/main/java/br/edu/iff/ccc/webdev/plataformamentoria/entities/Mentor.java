// src/main/java/br/edu/iff/mentorplatform/entities/Mentor.java
package br.edu.iff.ccc.webdev.plataformamentoria.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Mentor extends Usuario {

    @NotBlank(message = "A especialidade não pode ser vazia.")
    private String especialidade;
    
    // Construtor padrão
    public Mentor() {}

    // Getters e Setters
    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }
}