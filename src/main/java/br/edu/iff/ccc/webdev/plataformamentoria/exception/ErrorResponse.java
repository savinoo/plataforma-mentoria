package br.edu.iff.ccc.webdev.plataformamentoria.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Classe para padronização de respostas de erro da API seguindo RFC 7807 (ProblemDetail)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Resposta de erro padronizada da API")
public class ErrorResponse {
    
    @Schema(description = "Código de status HTTP", example = "404")
    private int status;
    
    @Schema(description = "Título do erro", example = "Resource Not Found")
    private String title;
    
    @Schema(description = "Mensagem detalhada do erro", example = "Mentor não encontrado com id: '999'")
    private String detail;
    
    @Schema(description = "Timestamp da ocorrência do erro")
    private LocalDateTime timestamp;
    
    @Schema(description = "Caminho da requisição que causou o erro", example = "/api/v1/mentores/999")
    private String instance;
    
    @Schema(description = "Tipo do problema", example = "about:blank")
    private String type = "about:blank";
    
    @Schema(description = "Erros de validação de campos específicos")
    private Map<String, String> validationErrors;

    public ErrorResponse(int status, String title, String detail, LocalDateTime timestamp, String instance) {
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.timestamp = timestamp;
        this.instance = instance;
    }

    public ErrorResponse(int status, String title, String detail, LocalDateTime timestamp, String instance, Map<String, String> validationErrors) {
        this.status = status;
        this.title = title;
        this.detail = detail;
        this.timestamp = timestamp;
        this.instance = instance;
        this.validationErrors = validationErrors;
    }

    // Getters e Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDetail() { return detail; }
    public void setDetail(String detail) { this.detail = detail; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getInstance() { return instance; }
    public void setInstance(String instance) { this.instance = instance; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public Map<String, String> getValidationErrors() { return validationErrors; }
    public void setValidationErrors(Map<String, String> validationErrors) { this.validationErrors = validationErrors; }
}
