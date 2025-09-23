package com.superapp.booking_service.web;

import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(NoSuchElementException ex,
            HttpServletRequest request) {
        String url = (request != null) ? request.getRequestURL().toString() : null;

        List<Map<String, Object>> errors = List.of(Map.of(
                "class", "NoSuchElementException",
                "field", "",
                "violationMessage", ex.getMessage() != null ? ex.getMessage() : "Resource not found"));

        return responseBody(url, "Not Found", errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGeneric(Exception ex,
            HttpServletRequest request) {
        String url = (request != null) ? request.getRequestURL().toString() : null;

        List<Map<String, Object>> errors = List.of(Map.of(
                "class", ex.getClass().getSimpleName(),
                "field", "",
                "violationMessage", ex.getMessage() != null ? ex.getMessage() : "Unexpected error"));

        return responseBody(url, "Internal Server Error", errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ===== MVC: @RequestBody @Valid errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String url = (request != null) ? request.getRequestURL().toString() : null;
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.<String, Object>of(
                        "class", simpleParamType(ex),
                        "field", fe.getField(),
                        "violationMessage", fe.getDefaultMessage()))
                .toList();

        return responseBody(url, "Validation Errors", errors, HttpStatus.BAD_REQUEST);
    }

    // ===== Both MVC & WebFlux: Constraint violations on @Validated method params,
    // @PathVariable, etc.
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, Object> handleConstraintViolation(ConstraintViolationException ex,
            HttpServletRequest request) {
        String url = (request != null) ? request.getRequestURL().toString() : null;

        List<Map<String, Object>> errors = ex.getConstraintViolations().stream()
                .map(cv -> {
                    Map<String, Object> err = new LinkedHashMap<>();
                    err.put("class",
                            cv.getLeafBean() != null ? cv.getLeafBean().getClass().getSimpleName() : "Unknown");
                    err.put("field", toDotPath(cv.getPropertyPath()));
                    err.put("violationMessage", cv.getMessage());
                    return err;
                })
                .toList();

        return responseBody(url, "Validation Errors", errors, HttpStatus.BAD_REQUEST);
    }

    // ---- helpers
    private static Map<String, Object> responseBody(String url, String title, List<Map<String, Object>> errors,
            HttpStatus status) {
        URI uri = url != null ? URI.create(url) : URI.create("http://unknown/");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("host", uri.getHost() == null ? "unknown" : uri.getHost());
        body.put("resource", uri.getPath());
        body.put("title", title);
        body.put("errors", errors);
        body.put("status", status.value());
        body.put("timestamp", Instant.now());

        return body;
    }

    private static String toDotPath(Path path) {
        // Spring usually gives paths like method.param.field; keep the right-most
        // meaningful bits
        String joined = Stream.of(path.toString().split("\\."))
                .reduce((a, b) -> b) // last segment
                .orElse(path.toString());
        return joined;
    }

    private static String simpleParamType(MethodArgumentNotValidException ex) {
        // best-effort class name of the target object for the binding result
        Object target = ex.getBindingResult().getTarget();
        return target != null ? target.getClass().getSimpleName() : "Unknown";
    }
}
