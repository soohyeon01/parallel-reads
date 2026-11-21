import { useEffect, useState } from 'react'
import {
  saveProgress,
  updateTotalPages,
  deleteBook
} from '../api/client'

export default function BookCard({ book, onChanged }) {
    const [page, setPage] = useState(book.currentPage)
    useEffect(() => { setPage(book.currentPage) }, [book.currentPage])
    const [totalPages, setTotalPages] = useState('')
    const [busy, setBusy] = useState(false)


    // handleSaveProgress 전체 교체: POST/PATCH 분기 삭제 + 범위 검사
    async function handleSaveProgress() {
      if (page === '' || Number(page) < 0 || Number(page) > book.totalPages) {
        alert(`0 ~ ${book.totalPages} 사이의 페이지를 입력해 주세요.`)
        return
      }

      setBusy(true)

      try {
        await saveProgress(book.bookId, page)
        await onChanged()
      } catch (error) {
        alert(error.message)
      } finally {
        setBusy(false)
      }
    }

  async function handleSaveTotalPages() {
    if (!totalPages || Number(totalPages) <= 0) {
      alert('총 페이지 수를 올바르게 입력해 주세요.')
      return
    }

    setBusy(true)

    try {
      await updateTotalPages(book.bookId, totalPages)
      await onChanged()
    } catch (error) {
      alert(error.message)
    } finally {
      setBusy(false)
    }
  }

  async function handleDelete() {
    if (!confirm('이 책을 목록에서 삭제할까요?')) return

    try {
      await deleteBook(book.bookId)
      await onChanged()
    } catch (error) {
      alert(error.message)
    }
  }

  return (
    <article className="book-card">
      <div className="thumbnail-wrap">
        {book.thumbnailUrl ? (
          <img
            className="thumbnail"
            src={book.thumbnailUrl}
            alt={`${book.title} 표지`}
          />
        ) : (
          <div className="thumbnail-placeholder">NO IMAGE</div>
        )}
      </div>

      <div className="book-info">
        <div className="book-heading">
          <div>
            <h3>{book.title}</h3>
            <p className="muted">{book.authors || '저자 정보 없음'}</p>
          </div>

          <button
            className="icon-button"
            onClick={handleDelete}
            aria-label="도서 삭제"
          >
            ×
          </button>
        </div>

        {!book.totalPages ? (
          /* 총 페이지수 입력 UI */
          <div className="page-input">
            <input
              type="number"
              min="1"
              placeholder="총 페이지 수"
              value={totalPages}
              onChange={e => setTotalPages(e.target.value)}
            />
            <span className="muted">페이지</span>
            <button onClick={handleSaveTotalPages} disabled={busy}>
              {busy ? '저장 중' : '등록'}
            </button>
          </div>
        ) : (   /* 진행률 및 현재 페이지 입력 UI */
          <>
            <div className="progress-label">
              <span>
                {book.currentPage} / {book.totalPages} 페이지
              </span>
              <strong>{book.progressPercent}%</strong>
            </div>

            <div className="progress-track">
              <div
                className="progress-fill"
                style={{ width: `${book.progressPercent}%` }}
              />
            </div>

            <div className="page-input">
              <input
                type="number"
                min="0"
                max={book.totalPages}
                value={page}
                onChange={e => setPage(e.target.value)}
                aria-label="현재 읽은 페이지"
              />
              <span className="muted">페이지</span>
              <button onClick={handleSaveProgress} disabled={busy}>
                {busy ? '저장 중' : '기록'}
              </button>
            </div>
          </>
        )}
      </div>
    </article>
  )
}
