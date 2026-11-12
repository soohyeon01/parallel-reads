package com.parallelreads.progress.dto;

import com.parallelreads.progress.calculator.ProgressCalculator;
import com.parallelreads.progress.entity.ReadingProgress;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProgressResponse {

    private final Long bookId;
    private final String title;
    private final Integer currentPage;
    private final Integer totalPages;
    private final Double progressPercent;
    private final LocalDateTime updatedAt;

    public ProgressResponse(ReadingProgress progress) {
        this.bookId = progress.getBook().getId();
        this.title = progress.getBook().getTitle();
        this.currentPage = progress.getCurrentPage();
        this.totalPages = progress.getBook().getTotalPages();
        this.progressPercent = ProgressCalculator.calculate(
                currentPage, totalPages
        );
        this.updatedAt = progress.getUpdatedAt();
    }
}