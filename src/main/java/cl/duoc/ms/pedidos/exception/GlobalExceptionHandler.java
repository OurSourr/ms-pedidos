package cl.duoc.ms.pedidos.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de excepciones de negocio (Carrito vacío, Stock insuficiente, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "REGLA_NEGOCIO_ERROR");
        response.put("mensaje", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(response);
    }

    // 2. Errores de validación de DTOs (@Valid en Controllers)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "ERROR_VALIDACION");
        response.put("mensaje", "Error de validación en los datos enviados");

        Map<String, String> erroresCampos = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> erroresCampos.put(err.getField(), err.getDefaultMessage()));

        response.put("detalles", erroresCampos);
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(response);
    }

    // 3. JSON mal formado enviado desde la App Móvil
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJson(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "JSON_INVALIDO");
        response.put("mensaje", "El JSON enviado en la petición es inválido");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(response);
    }

    // 4. Fallos en llamadas WebClient hacia el microservicio ms-catalogo
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleWebClient(WebClientResponseException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "ERROR_CATALOGO");
        response.put("mensaje", "Fallo en la comunicación con el microservicio de catálogo");
        response.put("status", ex.getStatusCode().value());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    // 5. Parámetros o argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "PETICION_INVALIDA");
        response.put("mensaje", ex.getMessage());
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(response);
    }

    // 6. Error no controlado (Fallback general)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) throws Exception {
        if (ex.getClass().getName().contains("NoResourceFoundException") || 
            ex instanceof jakarta.servlet.ServletException) {
            throw ex;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("error", "INTERNAL_ERROR");
        response.put("mensaje", "Error interno del servidor");
        response.put("timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}