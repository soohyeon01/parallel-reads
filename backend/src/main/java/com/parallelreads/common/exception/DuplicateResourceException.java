package com.parallelreads.common.exception;

// 이미 존재하는 데이터를 중복 등록하려 할 때 (409)
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}