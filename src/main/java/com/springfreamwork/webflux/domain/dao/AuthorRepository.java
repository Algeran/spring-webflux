package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Mono<Author> findByNameAndSurname(String name, String surname);

}
