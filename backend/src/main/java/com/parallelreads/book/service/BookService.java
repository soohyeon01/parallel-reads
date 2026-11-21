package com.parallelreads.book.service;

import com.parallelreads.book.dto.BookCreateRequest;
import com.parallelreads.book.dto.BookResponse;
import com.parallelreads.book.entity.Book;
import com.parallelreads.book.repository.BookRepository;
import com.parallelreads.common.exception.DuplicateResourceException;
import com.parallelreads.common.exception.InvalidRequestException;
import com.parallelreads.common.exception.ResourceNotFoundException;
import com.parallelreads.progress.repository.ReadingProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReadingProgressRepository readingProgressRepository;

    public List<BookResponse> findAll() {
        return bookRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(BookResponse::from)
                .toList();
    }

    public BookResponse findById(Long bookId) {
        return BookResponse.from(findBookOrThrow(bookId));
    }

    @Transactional
    public BookResponse create(BookCreateRequest request) {
        if (request.googleBookId() != null
                && bookRepository.existsByGoogleBookId(request.googleBookId())) {
            throw new DuplicateResourceException("이미 추가된 도서입니다.");
        }

        Book book = new Book(
                request.googleBookId(), request.title(), request.authors(),
                request.publisher(), request.description(),
                request.thumbnailUrl(), request.totalPages()
        );

        return BookResponse.from(bookRepository.save(book));
    }

    @Transactional
    public void updateTotalPages(Long bookId, Integer totalPages) {
        Book book = findBookOrThrow(bookId);

        readingProgressRepository.findByBookId(bookId)
                .filter(progress -> progress.getCurrentPage() > totalPages)
                .ifPresent(progress -> {
                    throw new InvalidRequestException(
                            "총 페이지 수는 현재 읽은 페이지(" + progress.getCurrentPage() + ")보다 작을 수 없습니다.");
                });

        book.updateTotalPages(totalPages);
    }

    @Transactional
    public void delete(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("존재하지 않는 도서입니다.");
        }

        bookRepository.deleteById(bookId);
    }

    // 별도의 검증 메서드로 분리
    private Book findBookOrThrow(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 도서입니다."));
    }

    /* 변환 로직을 DTO로 이동 */
}