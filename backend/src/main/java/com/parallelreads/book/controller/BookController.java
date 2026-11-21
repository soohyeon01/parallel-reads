package com.parallelreads.book.controller;

import com.parallelreads.book.dto.BookCreateRequest;
import com.parallelreads.book.dto.BookResponse;
import com.parallelreads.book.dto.TotalPagesUpdateRequest;
import com.parallelreads.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public BookResponse findById(@PathVariable Long bookId) {
        return bookService.findById(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookCreateRequest request) {
        return bookService.create(request);
    }

    @PatchMapping("/{bookId}/total-pages")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTotalPages(@PathVariable Long bookId,
                                 @Valid @RequestBody TotalPagesUpdateRequest request) {
        bookService.updateTotalPages(bookId, request.totalPages());
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long bookId) {
        bookService.delete(bookId);
    }
}