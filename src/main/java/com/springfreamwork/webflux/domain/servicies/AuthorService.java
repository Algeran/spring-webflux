package com.springfreamwork.webflux.domain.servicies;

import com.springfreamwork.webflux.domain.model.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface AuthorService {

    Flux<Author> getAllAuthors();

    Mono<Long> countAuthors();

    Mono<Void> createAuthor(Author author);

    Mono<Author> getAuthorById(String authorId);

    Flux<Author> getAuthorsById(Set<String> authorsId);

    Mono<Void> updateAuthor(Author author);

    Mono<Void> deleteAuthor(String id);
}
