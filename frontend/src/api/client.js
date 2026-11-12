async function request(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    }
  })

  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || '요청 처리 중 오류가 발생했습니다.')
  }

  if (response.status === 204) return null

  const text = await response.text()
  return text ? JSON.parse(text) : null
}

export const getDashboard = () => request('/api/dashboard')

export const searchBooks = (query) =>
  request(`/api/external/books/search?query=${encodeURIComponent(query)}`)

export const addBook = (book) =>
  request('/api/books', {
   method: 'POST', body: JSON.stringify(book)
  })

export const saveProgress = (bookId, currentPage) =>
  request(`/api/books/${bookId}/progress`, {
    method: 'POST',
    body: JSON.stringify({ currentPage: Number(currentPage) })
  })

export const updateProgress = (bookId, currentPage) =>
  request(`/api/books/${bookId}/progress`, {
    method: 'PATCH',
    body: JSON.stringify({ currentPage: Number(currentPage) })
  })

export const updateTotalPages = (bookId, totalPages) =>
  request(`/api/books/${bookId}/total-pages`, {
    method: 'PATCH',
    body: JSON.stringify({ totalPages: Number(totalPages) })
  })

export const deleteBook = (bookId) =>
  request(`/api/books/${bookId}`, { method: 'DELETE' })
