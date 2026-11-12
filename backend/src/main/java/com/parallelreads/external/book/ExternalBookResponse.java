package com.parallelreads.external.book;

// 필드명 BookCreaterequest 와 동일
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