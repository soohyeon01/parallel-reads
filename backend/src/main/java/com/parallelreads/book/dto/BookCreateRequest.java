package com.parallelreads.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BookCreateRequest(

        @Size(max = 100)
        String googleBookId,

        @NotBlank(message = "도서명은 필수입니다.")
        @Size(max = 500)
        String title,

        @Size(max = 500)
        String authors,

        String publisher,

        String description,

        String thumbnailUrl,

        @Positive(message = "전체 페이지 수는 0보다 커야 합니다.")
        Integer totalPages

) {
}