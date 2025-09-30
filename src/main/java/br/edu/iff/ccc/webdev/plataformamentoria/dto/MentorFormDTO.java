package br.edu.iff.ccc.webdev.plataformamentoria.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para criação de um novo Mentor")
public class MentorFormDTO {

    @Schema(description = "Primeiro nome do mentor.", example = "João")
    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @Schema(description = "Sobrenome do mentor.", example = "Silva")
    @NotBlank(message = "O sobrenome não pode ser vazio.")
    private String sobrenome;

    @Schema(description = "Endereço de email profissional do mentor.", example = "joao.silva@empresa.com")
    @NotBlank(message = "O email não pode ser vazio.")
    @Email(message = "Email inválido.")
    private String email;

    @Schema(description = "Senha de acesso do mentor. Deve ter no mínimo 12 caracteres, incluindo maiúscula, minúscula, número e caractere especial.", example = "Senha@Forte123")
    @NotBlank(message = "A senha não pode ser vazia.")
    @Size(min = 12, message = "A senha deve ter no mínimo 12 caracteres.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
             message = "A senha deve conter ao menos uma letra maiúscula, uma minúscula, um número e um caractere especial.")
    private String senha;

    @Schema(description = "Confirmação da senha.", example = "Senha@Forte123")
    @NotBlank(message = "A confirmação de senha não pode ser vazia.")
    private String confirmacaoSenha;

    @Schema(description = "Área de especialidade principal do mentor.", example = "Engenharia de Software")
    @NotBlank(message = "A especialidade não pode ser vazia.")
    private String especialidade;

    // Getters and Setters
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