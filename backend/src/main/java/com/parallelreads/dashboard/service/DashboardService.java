package com.parallelreads.dashboard.service;

import com.parallelreads.book.entity.Book;
import com.parallelreads.book.repository.BookRepository;
import com.parallelreads.dashboard.dto.BookDashboardResponse;
import com.parallelreads.progress.calculator.ProgressCalculator;
import com.parallelreads.progress.entity.ReadingProgress;
import com.parallelreads.progress.repository.ReadingProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final BookRepository bookRepository;
    private final ReadingProgressRepository readingProgressRepository;

    public List<BookDashboardResponse> findDashboard() {

        List<Book> books = bookRepository.findAll();

        List<Long> bookIds = books.stream().map(Book::getId).toList();

        // bookId를 Key로 사용하는 Map
        List<ReadingProgress> progressList = bookIds.isEmpty() ? List.of() : readingProgressRepository.findAllByBookIdIn(bookIds);

        Map<Long, ReadingProgress> progressMap = progressList
                .stream().collect(Collectors.toMap(progress -> progress.getBook().getId(), Function.identity()));

        return books.stream().map(book -> {
            ReadingProgress progress = progressMap.get(book.getId());

            int currentPage = progress == null ? 0 : progress.getCurrentPage();

            Integer totalPages = book.getTotalPages();

            Double progressPercent = ProgressCalculator.calculate(currentPage, totalPages);

            return new BookDashboardResponse(
                    book.getId(), book.getTitle(), book.getAuthors(), book.getThumbnailUrl(),
                    currentPage, totalPages, progressPercent,
                    progress == null ? null : progress.getUpdatedAt(),
                    progress != null);
        }).toList();
    }
}