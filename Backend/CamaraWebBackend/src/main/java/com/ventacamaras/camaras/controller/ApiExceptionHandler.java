package com.ventacamaras.camaras.controller;

import com.ventacamaras.camaras.exception.ConflictException;
import com.ventacamaras.camaras.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.Instant;
import java.util.Map;
import java.util.LinkedHashMap;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> badRequest(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> notFound(ResourceNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> conflict(ConflictException ex) {
        return error(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConcurrencyFailureException.class})
    public ResponseEntity<Object> dataConflict(RuntimeException ex) {
        return error(HttpStatus.CONFLICT, "La operación entra en conflicto con los datos actuales");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> unauthorized() {
        return error(HttpStatus.UNAUTHORIZED, "Credenciales inválidas o cuenta no disponible");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> forbidden() {
        return error(HttpStatus.FORBIDDEN, "No tienes permisos para esta operación");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unexpected(Exception ex) {
        log.error("Error inesperado al procesar la solicitud", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> result = body(status, "Solicitud no válida");
        if (ex instanceof MethodArgumentNotValidException validation) {
            Map<String, String> fields = new LinkedHashMap<>();
            validation.getBindingResult().getFieldErrors().forEach(e ->
                    fields.putIfAbsent(e.getField(), e.getDefaultMessage()));
            result.put("errors", fields);
        }
        return new ResponseEntity<>(result, headers, status);
    }

    private ResponseEntity<Object> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(body(status, message));
    }

    private Map<String, Object> body(HttpStatusCode status, String message) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", status.value());
        result.put("message", message == null ? "Error inesperado" : message);
        result.put("timestamp", Instant.now().toString());
        return result;
    }
}
