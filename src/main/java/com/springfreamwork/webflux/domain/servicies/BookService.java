package com.springfreamwork.webflux.domain.servicies;

import com.springfreamwork.webflux.domain.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface BookService {

    Flux<Book> getAllBooks();

    Mono<Long> countBooks();

    Mono<Void> deleteBook(String id);

    Mono<Book> getBook(String id);

    Mono<Void> updateBook(Book book);

    Mono<Void> createBook(Book book);

    Flux<Book> getBooksById(Set<String> ids);
}
