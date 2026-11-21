package com.parallelreads.progress.dto;

import com.parallelreads.book.entity.Book;
import com.parallelreads.common.util.ProgressCalculator;
import com.parallelreads.progress.entity.ReadingProgress;

import java.time.LocalDateTime;

public record ReadingProgressResponse(

        Long bookId,
        String title,
        Integer currentPage,
        Integer totalPages,
        Double progressPercent,
        LocalDateTime updatedAt
) {

    public static ReadingProgressResponse from(ReadingProgress progress) {
        Book book = progress.getBook();

        return new ReadingProgressResponse(
                book.getId(),
                book.getTitle(),
                progress.getCurrentPage(),
                book.getTotalPages(),
                ProgressCalculator.percent(progress.getCurrentPage(), book.getTotalPages()),
                progress.getUpdatedAt()
        );
    }
}