package com.parallelreads.book.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_book_id", unique = true, length = 100)
    private String googleBookId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(length = 500)
    private String authors;

    @Column(length = 500)
    private String publisher;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail_url", columnDefinition = "TEXT")
    private String thumbnailUrl;

    @Column(name = "total_pages")
    private Integer totalPages;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Book(String googleBookId,
                String title,
                String authors,
                String publisher,
                String description,
                String thumbnailUrl,
                Integer totalPages) {

        this.googleBookId = googleBookId;
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.totalPages = totalPages;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // google api 에서 totalPage가 없는 정보를 받아올 경우 사용자가 직접 수정 가능
    public void updateTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}