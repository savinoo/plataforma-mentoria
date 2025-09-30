package br.edu.iff.ccc.webdev.plataformamentoria.exceptions;

import br.edu.iff.ccc.webdev.plataformamentoria.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/recurso-nao-encontrado"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        logger.warn("Recurso não encontrado: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/recurso-nao-encontrado"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ProblemDetail handleRegraDeNegocioException(RegraDeNegocioException ex) {
        logger.warn("Violação de regra de negócio: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Violação de regra de negócio");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/regra-de-negocio"));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        
        logger.warn("Erro de validação: {}", ex.getMessage());
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Dados de entrada inválidos");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("validationErrors", validationErrors);
        
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @NonNull HttpRequestMethodNotSupportedException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {
        
        logger.warn("Método HTTP não suportado: {} - Métodos suportados: {}", ex.getMethod(), ex.getSupportedMethods());
        
        String detail = String.format("Método '%s' não é suportado para esta URL. Métodos suportados: %s", 
                ex.getMethod(), 
                ex.getSupportedMethods() != null ? String.join(", ", ex.getSupportedMethods()) : "Nenhum");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, detail);
        problemDetail.setTitle("Method Not Allowed");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/method-not-allowed"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.warn("Violação de integridade de dados: {}", ex.getMessage());
        
        String detail = "Conflito de dados no banco de dados";
        String message = ex.getMessage().toLowerCase();
        
        // Verificar tipos específicos de violação
        if (message.contains("email") && (message.contains("unique") || message.contains("constraint"))) {
            detail = "Email já está sendo usado por outro usuário";
        } else if (message.contains("cpf") && (message.contains("unique") || message.contains("constraint"))) {
            detail = "CPF já está cadastrado no sistema";
        } else if (message.contains("telefone") && (message.contains("unique") || message.contains("constraint"))) {
            detail = "Telefone já está cadastrado no sistema";
        } else if (message.contains("unique") || message.contains("constraint")) {
            detail = "Dados já existem no sistema. Verifique se não há duplicação";
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
        problemDetail.setTitle("Data Integrity Violation");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/data-integrity-violation"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ProblemDetail handleTransactionSystemException(TransactionSystemException ex) {
        logger.warn("Erro de transação: {}", ex.getMessage());
        
        // Verificar se a causa raiz é uma DataIntegrityViolationException
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof DataIntegrityViolationException) {
            return handleDataIntegrityViolation((DataIntegrityViolationException) rootCause);
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erro de validação de dados na transação");
        problemDetail.setTitle("Transaction Error");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/transaction-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Argumento inválido: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Invalid Argument");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/invalid-argument"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        logger.warn("Estado inválido: {}", ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Business Rule Violation");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/business-rule-violation"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        logger.error("Erro de runtime: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno: " + ex.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(Exception ex) {
        logger.error("Erro não tratado: {}", ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://plataformamentoria.com/erros/internal-server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
}