package com.parallelreads.progress.service;

import com.parallelreads.book.entity.Book;
import com.parallelreads.book.repository.BookRepository;
import com.parallelreads.common.exception.InvalidRequestException;
import com.parallelreads.common.exception.ResourceNotFoundException;
import com.parallelreads.progress.dto.ReadingProgressResponse;
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

    private final ReadingProgressRepository readingProgressRepository;
    private final BookRepository bookRepository;

    // 로직 통합
    @Transactional
    public void save(Long bookId, int currentPage) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 도서입니다."));

        Integer totalPages = book.getTotalPages();

        // 진행률 계산의 통일성을 위해 총 페이지 수가 없으면 현재 페이지도 저장할 수 없다
        if (totalPages == null) {
            throw new InvalidRequestException("먼저 총 페이지 수를 입력해 주세요.");
        }

        if (currentPage > totalPages) {
            throw new InvalidRequestException("현재 페이지는 총 페이지 수를 초과할 수 없습니다.");
        }

        ReadingProgress progress = readingProgressRepository.findByBookId(bookId)
                .orElseGet(() -> readingProgressRepository.save(new ReadingProgress(book)));

        progress.updateCurrentPage(currentPage);
    }

    public ReadingProgressResponse findByBookId(Long bookId) {
        ReadingProgress progress = readingProgressRepository.findByBookId(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("독서 진행 정보가 존재하지 않습니다."));

        return ReadingProgressResponse.from(progress);
    }
}