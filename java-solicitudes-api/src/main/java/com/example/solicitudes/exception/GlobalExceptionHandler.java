package com.example.solicitudes.exception;

import com.example.solicitudes.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.solicitudes.exception.SolicitudNotFoundException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.example.solicitudes.exception.SolicitudProcessingException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(
            MethodArgumentNotValidException exception) {

        List<String> errores = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        ErrorResponse response = new ErrorResponse(
                "Bad Request",
                errores
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(SolicitudNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarSolicitudNoEncontrada(
            SolicitudNotFoundException exception) {

        ErrorResponse response = new ErrorResponse(
                "Not Found",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(InvalidSolicitudIdException.class)
    public ResponseEntity<ErrorResponse> manejarIdInvalido(
            InvalidSolicitudIdException exception) {

        ErrorResponse response = new ErrorResponse(
                "Id de entrada inválido",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(
            MethodArgumentTypeMismatchException exception) {

        ErrorResponse response = new ErrorResponse(
                "Id de entrada inválido",
                "El id debe ser un número entero mayor que cero"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(SolicitudProcessingException.class)
    public ResponseEntity<ErrorResponse> manejarErrorInterno(
            SolicitudProcessingException exception) {

        ErrorResponse response = new ErrorResponse(
                "Internal Server Error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}