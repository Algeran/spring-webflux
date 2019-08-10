package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface GenreRepository extends ReactiveMongoRepository<Genre,String> {

    Mono<Genre> findByName(String name);

}
