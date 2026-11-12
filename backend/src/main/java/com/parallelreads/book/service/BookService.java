package com.parallelreads.book.service;

import com.parallelreads.book.dto.BookCreateRequest;
import com.parallelreads.book.dto.BookResponse;
import com.parallelreads.book.entity.Book;
import com.parallelreads.book.repository.BookRepository;
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
    private final ReadingProgressRepository progressRepository;

    public List<BookResponse> findAll() {
        return bookRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toResponse).toList();
    }

    public BookResponse findById(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 도서입니다."));

        return toResponse(book);
    }

    @Transactional
    public BookResponse create(BookCreateRequest request) {

        // Google Books ID가 존재하고, 이미 등록된 도서인지 확인
        if (request.googleBookId() != null
            && !request.googleBookId().isBlank()
            && bookRepository.existsByGoogleBookId(request.googleBookId())) {
            throw new IllegalStateException("이미 서재에 등록된 도서입니다.");
        }

        Book book = new Book(
                request.googleBookId(),
                request.title(),
                request.authors(),
                request.publisher(),
                request.description(),
                request.thumbnailUrl(),
                request.totalPages()
        );

        Book savedBook = bookRepository.save(book);

        return toResponse(savedBook);
    }

    @Transactional
    public void delete(Long bookId) {
        bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 도서입니다."));
        bookRepository.deleteById(bookId);
    }

    @Transactional
    public void updateTotalPages(Long bookId, Integer totalPages) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("존재하지 않는 도서입니다.")
                );

        progressRepository.findByBookId(bookId).ifPresent(progress -> {
            if (progress.getCurrentPage() > totalPages) {
                throw new IllegalArgumentException(
                        "총 페이지 수는 현재 읽은 페이지보다 작을 수 없습니다."
                );
            }
        });

        book.updateTotalPages(totalPages);
    }

    // 엔티티 외부 노출 없이 DTO로 변환
    private BookResponse toResponse(Book book) {
        return new BookResponse(book.getId(), book.getGoogleBookId(), book.getTitle(),
                book.getAuthors(), book.getPublisher(), book.getDescription(),
                book.getThumbnailUrl(), book.getTotalPages(), book.getCreatedAt());
    }
}