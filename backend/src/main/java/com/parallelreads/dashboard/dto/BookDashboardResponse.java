package com.parallelreads.dashboard.dto;

import java.time.LocalDateTime;

public record BookDashboardResponse(
        Long bookId,
        String title,
        String authors,
        String thumbnailUrl,
        Integer currentPage,
        Integer totalPages,
        Double progressPercent,
        LocalDateTime updatedAt,
        boolean hasProgress
) {
}