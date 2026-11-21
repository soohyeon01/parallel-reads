package com.parallelreads.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    // 리소스가 존재하지 않을 때 (404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        return error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 이미 존재하는 데이터를 중복 등록하려 할 때 (409)
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    // 비즈니스 규칙에 어긋난 요청 (400)
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidRequestException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // DB 제약 위반(UNIQUE, 길이 초과 등). 내부 메시지는 노출하지 않고 로그만 남긴다.(409)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException e) {
        log.warn("DB 제약 조건 위반", e);
        return error(HttpStatus.CONFLICT, "이미 등록되었거나 저장할 수 없는 데이터입니다.");
    }

    // DTO Validation 실패 (메시지는 각 DTO의 message 값을 그대로 사용) (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("잘못된 요청입니다.");

        return error(HttpStatus.BAD_REQUEST, message);
    }

    // JSON 형식 오류, 숫자 자리에 문자가 온 경우 등
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ErrorResponse> handleUnreadable(Exception e) {
        return error(HttpStatus.BAD_REQUEST, "요청 형식이 올바르지 않습니다.");
    }

    // Google Books 호출 실패 (502)
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleExternalApi(RestClientException e) {
        log.error("외부 도서 API 호출 실패", e);
        return error(HttpStatus.BAD_GATEWAY, "도서 검색 서비스에 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.");
    }

    // 반환 타입 메서드
    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(message));
    }
}