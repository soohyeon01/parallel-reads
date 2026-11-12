package com.parallelreads.progress.calculator;

public final class ProgressCalculator {

    private ProgressCalculator() {
    }

    public static Double calculate(Integer currentPage, Integer totalPages) {
        if (totalPages == null || totalPages <= 0) {
            return null;
        }

        return Math.round((double) currentPage / totalPages * 1000) / 10.0;
    }
}