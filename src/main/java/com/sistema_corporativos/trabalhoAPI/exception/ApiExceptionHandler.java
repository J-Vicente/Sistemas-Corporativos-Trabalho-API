package com.sistema_corporativos.trabalhoAPI.exception;

import com.sistema_corporativos.trabalhoAPI.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  ResponseEntity<ErrorResponse> notFound(
      Exception exception, jakarta.servlet.http.HttpServletRequest request) {
    return buildResponse(404, exception.getMessage(), request);
  }

  @ExceptionHandler(BusinessRuleException.class)
  ResponseEntity<ErrorResponse> businessRuleConflict(
      Exception exception, jakarta.servlet.http.HttpServletRequest request) {
    return buildResponse(409, exception.getMessage(), request);
  }

  @ExceptionHandler({
    IllegalArgumentException.class,
    MethodArgumentNotValidException.class,
    org.springframework.http.converter.HttpMessageNotReadableException.class
  })
  ResponseEntity<ErrorResponse> invalidRequest(
      Exception exception, jakarta.servlet.http.HttpServletRequest request) {
    if (exception instanceof MethodArgumentNotValidException validationException) {
      String message =
          validationException.getBindingResult().getFieldErrors().stream()
              .map(error -> error.getField() + ": " + error.getDefaultMessage())
              .collect(Collectors.joining("; "));
      return buildResponse(400, message, request);
    }
    if (exception instanceof org.springframework.http.converter.HttpMessageNotReadableException) {
      return buildResponse(400, "Corpo da requisição inválido", request);
    }
    return buildResponse(400, exception.getMessage(), request);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<ErrorResponse> dataConflict(
      Exception exception, jakarta.servlet.http.HttpServletRequest request) {
    return buildResponse(409, "Registro duplicado ou relacionado a outros dados", request);
  }

  private ResponseEntity<ErrorResponse> buildResponse(
      int status, String message, jakarta.servlet.http.HttpServletRequest request) {
    return ResponseEntity.status(status)
        .body(new ErrorResponse(Instant.now(), status, message, request.getRequestURI()));
  }
}
