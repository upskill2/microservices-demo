package com.microservices.demo.elastic.query.api.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ElasticQueryErrorHandler {


    @ExceptionHandler (AccessDeniedException.class)
    public ResponseEntity<String> handle (AccessDeniedException ex) {
        log.error ("Access denied exception: {}", ex.getMessage ());
        return ResponseEntity.status (403).body ("You are not allowed to access this resource");
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<String> handle (IllegalArgumentException ex) {
        log.error ("Illegal argument exception: {}", ex.getMessage ());
        return ResponseEntity.badRequest ().body ("Illegal argument exception " + ex.getMessage ());
    }

    @ExceptionHandler (RuntimeException.class)
    public ResponseEntity<String> handle (RuntimeException ex) {
        log.error ("Exception: {}", ex.getMessage ());
        return ResponseEntity.badRequest ().body ("Exception " + ex.getMessage ());
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<String> handle (Exception ex) {
        log.error ("Exception: {}", ex.getMessage ());
        return ResponseEntity.badRequest ().body ("Exception " + ex.getMessage ());
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle (MethodArgumentNotValidException ex) {
        log.error ("Exception: {}", ex.getMessage ());
        Map<String, String> errors = new HashMap<> ();
        ex.getBindingResult ().getFieldErrors ().forEach (error ->
                errors.put (error.getField (), error.getDefaultMessage ()));
        return ResponseEntity.badRequest ().body (errors);
    }

}
