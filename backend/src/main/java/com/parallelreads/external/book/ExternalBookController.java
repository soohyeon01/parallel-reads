package com.parallelreads.external.book;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external/books")
public class ExternalBookController {

    private final GoogleBooksService googleBooksService;

    @GetMapping("/search")
    public List<ExternalBookResponse> search(@RequestParam String query) {
        if (query.isBlank()) {
            throw new IllegalArgumentException("검색어를 입력해 주세요.");
        }

        return googleBooksService.search(query.trim());
    }
}