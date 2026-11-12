CREATE TABLE books (
    id BIGSERIAL PRIMARY KEY,
    google_book_id VARCHAR(100) UNIQUE,
    title VARCHAR(500) NOT NULL,
    authors VARCHAR(500),
    publisher VARCHAR(255),
    description TEXT,
    thumbnail_url TEXT,
    total_pages INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_books_total_pages
        CHECK (total_pages IS NULL OR total_pages > 0)
);

CREATE TABLE reading_progress (
    id BIGSERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL UNIQUE,
    current_page INTEGER NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reading_progress_book
        FOREIGN KEY (book_id)
        REFERENCES books(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_current_page
        CHECK (current_page >= 0)
);