package com.parallelreads.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TotalPagesUpdateRequest(

        @NotNull(message = "현재 페이지를 입력해 주세요.")
        @Min(value = 0, message = "전체 페이지 수는 0보다 커야 합니다.")
        Integer totalPages

) {

}