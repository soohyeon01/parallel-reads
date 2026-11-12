package com.parallelreads.common.exception;

// 도서나 독서 진행 정보를 찾지 못했을 때
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}