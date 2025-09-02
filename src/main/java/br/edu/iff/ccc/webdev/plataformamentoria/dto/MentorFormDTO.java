package br.edu.iff.ccc.webdev.plataformamentoria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MentorFormDTO {

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @NotBlank(message = "O sobrenome não pode ser vazio.")
    private String sobrenome;

    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "Email inválido.")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Size(min = 12, message = "A senha deve ter no mínimo 12 caracteres.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
             message = "A senha deve conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial.")
    private String senha;

    @NotBlank(message = "A confirmação de senha não pode ser vazia.")
    private String confirmacaoSenha;

    @NotBlank(message = "A especialidade não pode ser vazia.")
    private String especialidade;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSobrenome() { return sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getConfirmacaoSenha() { return confirmacaoSenha; }
    public void setConfirmacaoSenha(String confirmacaoSenha) { this.confirmacaoSenha = confirmacaoSenha; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}