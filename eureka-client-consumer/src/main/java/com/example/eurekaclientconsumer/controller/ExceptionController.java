package com.example.eurekaclientconsumer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@EnableWebMvc
public class ExceptionController extends ResponseEntityExceptionHandler {

    /**
     * handleException - Handles all the Exception recieving a request,
     * responseWrapper.
     * 
     * @param request
     * @param responseWrapper
     * @return ResponseEntity<ResponseWrapper>
     * @user ihuaylupo
     * @since 2018-09-12
     */
    @ExceptionHandler(value = { Exception.class })
    public @ResponseBody ResponseEntity<Map<String, String>> handleException(HttpServletRequest request, Exception e) {
        return ResponseEntity.ok(Map.of("message", "exception happened", "exception", e.getClass().getName()));
    }

    /**
     * handleIOException - Handles all the Authentication Exceptions of the
     * application.
     * 
     * @param request
     * @param exception
     * @return ResponseEntity<ResponseWrapper>
     * @user ihuaylupo
     * @since 2018-09-12
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleIOException(HttpServletRequest request, RuntimeException e) {
        return ResponseEntity
                .ok(Map.of("status", HttpStatus.NOT_ACCEPTABLE, "message", e.getMessage(), "exception",
                        e.getClass().getName()));
    }

}
