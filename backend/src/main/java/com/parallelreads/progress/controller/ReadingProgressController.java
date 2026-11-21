package com.parallelreads.progress.controller;

import com.parallelreads.progress.dto.ReadingProgressResponse;
import com.parallelreads.progress.dto.ReadingProgressUpdateRequest;
import com.parallelreads.progress.service.ReadingProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books/{bookId}/progress")
public class ReadingProgressController {

    private final ReadingProgressService readingProgressService;

    // 최초 기록과 수정을 모두 처리
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@PathVariable Long bookId,
                     @Valid @RequestBody ReadingProgressUpdateRequest request) {
        readingProgressService.save(bookId, request.currentPage());
    }

    @GetMapping
    public ReadingProgressResponse findByBookId(@PathVariable Long bookId) {
        return readingProgressService.findByBookId(bookId);
    }
}