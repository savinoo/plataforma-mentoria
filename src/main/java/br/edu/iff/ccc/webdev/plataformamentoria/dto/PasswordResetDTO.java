package br.edu.iff.ccc.webdev.plataformamentoria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetDTO {

    @NotBlank
    private String token;

    @NotBlank(message = "A nova senha não pode ser vazia.")
    @Size(min = 12, message = "A senha deve ter no mínimo 12 caracteres.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
             message = "A senha deve conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial.")
    private String senha;

    @NotBlank(message = "A confirmação de senha não pode ser vazia.")
    private String confirmacaoSenha;

    // Getters e Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getConfirmacaoSenha() { return confirmacaoSenha; }
    public void setConfirmacaoSenha(String confirmacaoSenha) { this.confirmacaoSenha = confirmacaoSenha; }
}