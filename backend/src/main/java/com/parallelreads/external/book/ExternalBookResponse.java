package com.parallelreads.external.book;

// 필드명은 BookCreateRequest 와 동일하다 (프론트가 이 객체를 그대로 POST /api/books 로 전달)
public record ExternalBookResponse(
        String googleBookId,
        String title,
        String authors,
        String publisher,
        String description,
        String thumbnailUrl,
        Integer totalPages
) {
}