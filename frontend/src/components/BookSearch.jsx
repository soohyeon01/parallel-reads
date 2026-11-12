
import { useState } from 'react'
import { searchBooks, addBook } from '../api/client'

export default function BookSearch({ onAdded, onClose }) {
  const [query, setQuery] = useState('')
  const [results, setResults] = useState([])
  const [loading, setLoading] = useState(false)
  const [addingId, setAddingId] = useState(null)
  const [searched, setSearched] = useState(false)

  async function handleSearch(e) {
    e.preventDefault()

    if (!query.trim()) return

    setLoading(true)
    setSearched(true)
    setResults([])

    try {
      const data = await searchBooks(query.trim())
      setResults(data)
    } catch (error) {
      alert(error.message)
    } finally {
      setLoading(false)
    }
  }

  async function handleAdd(book) {
    setAddingId(book.googleBookId)

    try {
      await addBook(book)
      await onAdded()
      onClose()
    } catch (error) {
      alert(error.message)
    } finally {
      setAddingId(null)
    }
  }

  return (
    <section className="search-panel">
      <div className="panel-heading">
        <h2>책 검색하기</h2>
        <button className="icon-button" onClick={onClose}>×</button>
      </div>

      <form className="search-form" onSubmit={handleSearch}>
        <input
          value={query}
          onChange={e => setQuery(e.target.value)}
          placeholder="책 제목, 저자 또는 ISBN을 입력하세요"
        />
        <button disabled={loading}>
          {loading ? '검색 중...' : '검색'}
        </button>
      </form>

      <div className="search-results">
        {results.map(book => (
          <div className="search-result" key={book.googleBookId}>
            {book.thumbnailUrl ? (
              <img src={book.thumbnailUrl} alt={`${book.title} 표지`} />
            ) : (
              <div className="mini-cover">NO IMAGE</div>
            )}

            <div className="result-info">
              <strong>{book.title}</strong>

              <span className="muted">
                {book.authors || '저자 정보 없음'}
              </span>

              <span className="muted">
                {book.publisher || '출판사 정보 없음'}
              </span>

              <span className="muted">
                {book.totalPages
                  ? `${book.totalPages}쪽`
                  : '쪽수 정보 없음'}
              </span>
            </div>

            {/* 등록중임을 표시해서 중복 클릭을 방지 */}
            <button
              onClick={() => handleAdd(book)}
              disabled={addingId !== null}
            >
              {addingId === book.googleBookId ? '추가 중...' : '추가'}
            </button>
          </div>
        ))}

       {/* 검색 결과가 없을 경우, 없음을 반환 */}
        {!loading && searched && results.length === 0 && (
          <p className="muted empty">검색 결과가 없습니다.</p>
        )}

        {!loading && !searched && (
          <p className="muted empty">
            책 제목이나 저자를 검색해 보세요.
          </p>
        )}
      </div>
    </section>
  )
}
