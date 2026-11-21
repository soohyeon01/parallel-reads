package com.parallelreads.common.util;

public final class ProgressCalculator {

    private ProgressCalculator() {
    }

    // 총 페이지 수를 모르면 null
    public static Double percent(Integer currentPage, Integer totalPages) {
        if (totalPages == null || totalPages <= 0) {
            return null;
        }

        return Math.round((double) currentPage / totalPages * 1000) / 10.0;
    }
}