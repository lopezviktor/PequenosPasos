package com.pequenospasos.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejar IllegalArgumentException correctamente como un BAD REQUEST (400)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // Manejar RuntimeException, pero devolviendo un 400 si el mensaje lo amerita
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Si la excepción es por validación de negocio, cambiar a 400
        if (ex.getMessage() != null && (
                ex.getMessage().contains("Debe asignar un educador") ||
                        ex.getMessage().contains("Solo un EDUCADOR puede registrar comidas.") ||
                        ex.getMessage().contains("Comida no encontrada")
        )) {
            status = HttpStatus.BAD_REQUEST;
        }

        return buildErrorResponse(status, status.getReasonPhrase(), ex.getMessage());
    }

    // Manejar validaciones de RequestBody incorrecto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Error");

        // Extraer los mensajes de error de validación
        ex.getBindingResult().getFieldErrors().forEach(error ->
                response.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(response);
    }

    // Manejar NullPointerException con el mensaje real
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleNullPointerException(NullPointerException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Se ha producido un error inesperado: " + ex.getMessage());
    }

    // Método auxiliar para construir respuestas de error
    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }

    // Manejar BadCredentialsException para autenticación fallida
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(BadCredentialsException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", "Credenciales incorrectas");
    }
}