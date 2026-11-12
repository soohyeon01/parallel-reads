package com.parallelreads.progress.controller;

import com.parallelreads.progress.dto.ProgressResponse;
import com.parallelreads.progress.dto.ProgressUpdateRequest;
import com.parallelreads.progress.service.ReadingProgressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books/{bookId}/progress")
public class ReadingProgressController {

    private final ReadingProgressService readingProgressService;

    // 최초 등록 전용으로 유지, 프론트에서 등록 여부 구분
    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable Long bookId,
            @Valid @RequestBody ProgressUpdateRequest request) {
        readingProgressService.create(bookId, request.currentPage());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(
            @PathVariable Long bookId,
            @Valid @RequestBody ProgressUpdateRequest request
    ) {
        readingProgressService.updateCurrentPage(bookId, request.currentPage());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ProgressResponse> findProgress(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(readingProgressService.findProgress(bookId));
    }
}