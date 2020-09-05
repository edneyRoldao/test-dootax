package com.dootax.teste.config;

import com.dootax.teste.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleAllExceptions(Exception e, WebRequest req) {
        StackTraceElement stl  = e.getStackTrace()[0];
        String baseMsg = "Ocorreu um erro inesperado. CLASS: %s, METHOD: %s, LINE: %s";

        log.error(String.format(baseMsg, stl.getClassName(), stl.getMethodName(), stl.getLineNumber()));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocorreu um erro inesperado. Tente novamente mais tarde");
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<String> handleBadRequestException(BadRequestException e, WebRequest req) {
        log.info("Ocorreu um badRequest. {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
