package com.parallelreads.progress.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProgressUpdateRequest(
        @NotNull(message = "현재 페이지를 입력해 주세요.")
        @Min(value = 0, message = "현재 페이지는 0 이상이어야 합니다.")
        Integer currentPage

) {

}