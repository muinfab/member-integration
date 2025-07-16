package com.company.membership.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 첫 번째 오류 메시지 추출 (에러코드|메시지 형태)
        String rawMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("E400|요청 값이 올바르지 않습니다.");

        String code = "400";
        String message = rawMessage;
        if (rawMessage.contains("|")) {
            String[] parts = rawMessage.split("\\|", 2);
            code = parts[0];
            message = parts[1];
        }

        ErrorResponse errorResponse = new ErrorResponse(code, message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 명세에 맞는 응답 구조
    public static class ErrorResponse {
        private String code;
        private String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }
        public String getCode() { return code; }
        public String getMessage() { return message; }
        public void setCode(String code) { this.code = code; }
        public void setMessage(String message) { this.message = message; }
    }
} 