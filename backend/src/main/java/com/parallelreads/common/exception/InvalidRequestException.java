package com.parallelreads.common.exception;

// 비즈니스 규칙에 어긋난 요청 (400)
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}