package com.parallelreads.progress.repository;

import com.parallelreads.progress.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {

    Optional<ReadingProgress> findByBookId(Long bookId);

    List<ReadingProgress> findAllByBookIdIn(List<Long> bookIds);
}