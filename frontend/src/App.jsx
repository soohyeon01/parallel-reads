import { useEffect, useState } from 'react'
import { getDashboard } from './api/client'
import BookCard from './components/BookCard'
import BookSearch from './components/BookSearch'

export default function App() {
  const [books, setBooks] = useState([])
  const [showSearch, setShowSearch] = useState(false)
  const [loading, setLoading] = useState(true)

  async function loadBooks() {
    try {
      setBooks(await getDashboard())
    } catch (error) {
      alert(error.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { loadBooks() }, [])

  const completed = books.filter(book => book.progressPercent >= 100).length

  const booksWithProgress = books.filter(
    book => book.progressPercent !== null
  )

  const average = booksWithProgress.length
    ? (
        booksWithProgress.reduce(
            (sum, book) => sum + book.progressPercent,
            0
        )  / booksWithProgress.length
      ).toFixed(1)
    : '0.0'


  return (
    <main className="app-shell">
      <header className="topbar">
        <a className="brand" href="/">parallel reads<span>.</span></a>
        <span className="topbar-note">나만의 병렬 독서 기록장</span>
      </header>

      <section className="hero">
        <div>
          <p className="eyebrow">YOUR READING SPACE</p>
          <h1>여러 권을 읽어도,<br />나만의 속도로.</h1>
          <p className="hero-copy">지금 읽고 있는 책들의 흐름을 한눈에 확인하세요.</p>
        </div>
        <button className="add-button" onClick={() => setShowSearch(v => !v)}>
          + 책 추가하기
        </button>
      </section>

      <section className="stats">
        <div className="stat"><span>읽고 있는 책</span><strong>{books.length}<small>권</small></strong></div>
        <div className="stat"><span>평균 진행률</span><strong>{average}<small>%</small></strong></div>
        <div className="stat"><span>완독한 책</span><strong>{completed}<small>권</small></strong></div>
      </section>

      {showSearch && <BookSearch onAdded={loadBooks} onClose={() => setShowSearch(false)} />}

      <section>
        <div className="section-heading">
          <div><p className="eyebrow">MY LIBRARY</p><h2>읽고 있는 책</h2></div>
          <span className="muted">{books.length} books</span>
        </div>
        {loading ? <p className="muted">불러오는 중...</p> :
          books.length === 0
            ? <div className="empty-state"><div className="empty-icon">▤</div>
                <h3>아직 등록된 책이 없어요.</h3><p>첫 번째 책을 추가해 독서 기록을 시작해 보세요.</p>
                <button onClick={() => setShowSearch(true)}>책 검색하기</button>
              </div>
            : <div className="book-list">
                {books.map(book => <BookCard key={book.bookId} book={book} onChanged={loadBooks} />)}
              </div>
        }
      </section>
      <footer>PARALLEL READS · READ AT YOUR OWN PACE</footer>
    </main>
  )
}
