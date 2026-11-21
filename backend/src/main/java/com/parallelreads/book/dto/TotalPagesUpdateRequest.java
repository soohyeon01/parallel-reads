package com.parallelreads.book.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// BookCreateRequest 와 형식 통일
public record TotalPagesUpdateRequest(

        @NotNull(message = "총 페이지 수는 필수입니다.")
        @Positive(message = "총 페이지 수는 0보다 커야 합니다.")
        Integer totalPages

) {

}