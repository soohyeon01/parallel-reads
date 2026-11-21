package com.parallelreads.book.dto;

import com.parallelreads.book.entity.Book;

import java.time.LocalDateTime;

public record BookResponse(
        Long id,
        String googleBookId,
        String title,
        String authors,
        String publisher,
        String description,
        String thumbnailUrl,
        Integer totalPages,
        LocalDateTime createdAt
) {

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getGoogleBookId(),
                book.getTitle(),
                book.getAuthors(),
                book.getPublisher(),
                book.getDescription(),
                book.getThumbnailUrl(),
                book.getTotalPages(),
                book.getCreatedAt()
        );
    }
}
