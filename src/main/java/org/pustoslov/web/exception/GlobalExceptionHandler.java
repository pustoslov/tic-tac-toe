package org.pustoslov.web.exception;

import org.pustoslov.domain.exception.GameNotFoundException;
import org.pustoslov.domain.exception.IllegalMoveException;
import org.pustoslov.domain.exception.NoAccessException;
import org.pustoslov.web.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalMoveException.class)
  public ResponseEntity<ErrorResponse> handleCorruptedGame(IllegalMoveException ex) {
    ErrorResponse response = new ErrorResponse("Illegal move", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
  }

  @ExceptionHandler(NoAccessException.class)
  public ResponseEntity<ErrorResponse> handleCorruptedGame(NoAccessException ex) {
    ErrorResponse response = new ErrorResponse("Access exception", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
  }

  @ExceptionHandler(GameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex) {
    ErrorResponse response = new ErrorResponse("Game not found", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    FieldError fieldError = ex.getFieldError();
    String message =
        fieldError == null ? "" : fieldError.getField() + " " + fieldError.getDefaultMessage();
    ErrorResponse response = new ErrorResponse("Validation failed", message);
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    ErrorResponse response = new ErrorResponse("Internal server error", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
