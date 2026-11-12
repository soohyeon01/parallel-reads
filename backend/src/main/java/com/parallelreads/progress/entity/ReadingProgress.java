package com.parallelreads.progress.entity;

import com.parallelreads.book.entity.Book;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "readingProgress")
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false, unique = true)
    private Book book;

    @Column(nullable = false)
    private int currentPage = 0;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public ReadingProgress(Book book, int currentPage) {
        this.book = book;
        this.currentPage = currentPage;
    }

    public void updateCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}