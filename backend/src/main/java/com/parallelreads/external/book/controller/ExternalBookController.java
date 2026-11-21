package com.parallelreads.external.book.controller;

import com.parallelreads.common.exception.InvalidRequestException;
import com.parallelreads.external.book.ExternalBookResponse;
import com.parallelreads.external.book.client.GoogleBooksClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external/books")
public class ExternalBookController {

    private final GoogleBooksClient googleBooksClient;

    @GetMapping("/search")
    public List<ExternalBookResponse> search(@RequestParam String query) {
        if (query.isBlank()) {
            throw new InvalidRequestException("검색어를 입력해 주세요.");
        }

        return googleBooksClient.search(query.trim());
    }
}