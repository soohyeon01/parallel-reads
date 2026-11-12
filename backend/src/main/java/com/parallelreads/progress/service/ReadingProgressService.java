package com.parallelreads.progress.service;

import com.parallelreads.book.entity.Book;
import com.parallelreads.book.repository.BookRepository;
import com.parallelreads.common.exception.ResourceNotFoundException;
import com.parallelreads.progress.dto.ProgressResponse;
import com.parallelreads.progress.entity.ReadingProgress;
import com.parallelreads.progress.repository.ReadingProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * DB에는 currentPage만 저장하고, 진행률은 조회시 계산
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadingProgressService {

    private final ReadingProgressRepository progressRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void create(Long bookId, Integer currentPage) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 도서입니다."));

        if (progressRepository.findByBookId(bookId).isPresent()) {
            throw new IllegalStateException("이미 독서 진행 정보가 존재합니다.");
        }

        Integer totalPages = book.getTotalPages();

        if (totalPages == null) {
            throw new IllegalStateException("먼저 총 페이지 수를 입력해 주세요.");
        }

        if (currentPage > totalPages) {
            throw new IllegalArgumentException(
                    "현재 페이지는 총 페이지 수를 초과할 수 없습니다."
            );
        }

        progressRepository.save(new ReadingProgress(book, currentPage));
    }

    @Transactional
    public void updateCurrentPage(Long bookId, Integer currentPage) {
        ReadingProgress progress = progressRepository.findByBookId(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("독서 진행 정보가 존재하지 않습니다.")
                );

        Integer totalPages = progress.getBook().getTotalPages();

        // 진행률 계산의 통일성을 위해 총 페이지 수가 없으면 현재 페이지도 수정할 수 없도록 설계
        if (totalPages == null) {
            throw new IllegalStateException("먼저 총 페이지 수를 입력해 주세요.");
        }

        if (currentPage > totalPages) {
            throw new IllegalArgumentException("현재 페이지는 총 페이지 수를 초과할 수 없습니다.");
        }

        progress.updateCurrentPage(currentPage);
    }

    public ProgressResponse findProgress(Long bookId) {
        ReadingProgress progress = progressRepository.findByBookId(bookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("독서 진행 정보가 존재하지 않습니다.")
                );

        return new ProgressResponse(progress);
    }
}