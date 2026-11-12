package com.parallelreads.book.repository;

import com.parallelreads.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByGoogleBookId(String googleBookId);

    List<Book> findAllByOrderByCreatedAtDesc();
}