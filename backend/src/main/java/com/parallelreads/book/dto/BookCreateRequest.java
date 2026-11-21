package com.parallelreads.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BookCreateRequest(

        @Size(max = 100)
        String googleBookId,

        @NotBlank(message = "도서명은 필수입니다.")
        @Size(max = 500, message = "도서명은 500자 이하여야 합니다.")
        String title,

        @Size(max = 500, message = "저자는 500자 이하여야 합니다.")
        String authors,

        @Size(max = 255, message = "출판사는 255자 이하여야 합니다.")
        String publisher,

        String description,

        String thumbnailUrl,

        @Positive(message = "총 페이지 수는 0보다 커야 합니다.")
        Integer totalPages

) {
}