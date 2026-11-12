package com.parallelreads.book.controller;

import com.parallelreads.book.dto.BookCreateRequest;
import com.parallelreads.book.dto.BookResponse;
import com.parallelreads.book.dto.TotalPagesUpdateRequest;
import com.parallelreads.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.findById(bookId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookCreateRequest request) {
        return bookService.create(request);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long bookId) {
        bookService.delete(bookId);
    }

    @PatchMapping("/{bookId}/total-pages")
    public ResponseEntity<Void> updateTotalPages(
            @PathVariable Long bookId,
            @Valid @RequestBody TotalPagesUpdateRequest request
    ) {
        bookService.updateTotalPages(bookId, request.totalPages());
        return ResponseEntity.noContent().build();
    }
}