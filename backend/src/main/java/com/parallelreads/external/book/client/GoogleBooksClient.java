package com.parallelreads.external.book.client;

import com.parallelreads.external.book.ExternalBookResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

@Component
public class GoogleBooksClient {

    // 외부 HTTP API를 호출하는 클라이언트
    private final RestClient restClient;
    private final String apiKey;

    // 키가 없어도 기동되고, 응답이 없을 때 무한정 기다리지 않도록 타임아웃을 건다
    public GoogleBooksClient(@Value("${google.books.api-key:}") String apiKey) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));

        this.apiKey = apiKey;
        this.restClient = RestClient.builder()
                .baseUrl("https://www.googleapis.com")
                .requestFactory(factory)
                .build();
    }

    public List<ExternalBookResponse> search(String query) {

        GoogleBooksResponse response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/books/v1/volumes")
                            .queryParam("q", query)
                            .queryParam("maxResults", 20)
                            .queryParam("printType", "books");

                    if (!apiKey.isBlank()) {
                        uriBuilder.queryParam("key", apiKey);
                    }

                    return uriBuilder.build();
                })
                .retrieve()
                .body(GoogleBooksResponse.class);

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
                truncate(info.title(), 500),
                truncate(authors, 500),
                truncate(info.publisher, 255),
                info.description(),
                thumbnailUrl,
                totalPages
        );
    }

    private String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }

        return value.substring(0, max);
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