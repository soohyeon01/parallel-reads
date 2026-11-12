package com.parallelreads.external.book;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class GoogleBooksService {

    // 외부 HTTP API를 호출하는 클라이언트
    private final RestClient restClient;
    private final String apiKey;

    public GoogleBooksService(@Value("${google.books.api-key}") String apiKey) {
        this.apiKey = apiKey;
        this.restClient = RestClient.create("https://www.googleapis.com");
    }

    public List<ExternalBookResponse> search(String query) {

        GoogleBooksResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/books/v1/volumes")
                        .queryParam("q", query)
                        .queryParam("maxResults", 20)
                        .queryParam("printType", "books")
                        .queryParam("key", apiKey)
                        .build())
                .retrieve()
                .body(GoogleBooksResponse.class);   // JSON 응답을 Java 객체로 변환

        if (response == null || response.items() == null) {
            return List.of();
        }

        return response.items().stream()
                .filter(item -> item.volumeInfo() != null)
                .filter(item -> item.volumeInfo().title() != null)
                .map(this::toResponse)
                .toList();
    }

    private ExternalBookResponse toResponse(GoogleBookItem item) {
        GoogleVolumeInfo info = item.volumeInfo();

        String authors = info.authors() == null
                ? null
                : String.join(", ", info.authors());

        String thumbnailUrl = null;

        if (info.imageLinks() != null) {
            thumbnailUrl = info.imageLinks().thumbnail();

            if (thumbnailUrl == null) {
                thumbnailUrl = info.imageLinks().smallThumbnail();
            }

            if (thumbnailUrl != null) {
                thumbnailUrl = thumbnailUrl.replaceFirst("^http://", "https://");
            }
        }

        Integer totalPages = info.pageCount() != null && info.pageCount() > 0
                ? info.pageCount()
                : null;

        return new ExternalBookResponse(
                item.id(),
                info.title(),
                authors,
                info.publisher(),
                info.description(),
                thumbnailUrl,
                totalPages
        );
    }

    private record GoogleBooksResponse(List<GoogleBookItem> items) {
    }

    private record GoogleBookItem(
            String id,
            GoogleVolumeInfo volumeInfo
    ) {
    }

    private record GoogleVolumeInfo(
            String title,
            List<String> authors,
            String publisher,
            String description,
            Integer pageCount,
            GoogleImageLinks imageLinks
    ) {
    }

    private record GoogleImageLinks(
            String thumbnail,
            String smallThumbnail
    ) {
    }
}