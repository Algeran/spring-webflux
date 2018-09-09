package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Mono<Book> findByName(String name);
}
