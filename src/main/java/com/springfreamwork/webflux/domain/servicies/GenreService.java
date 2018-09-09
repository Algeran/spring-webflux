package com.springfreamwork.webflux.domain.servicies;

import com.springfreamwork.webflux.domain.model.Genre;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenreService {

    Mono<Void> createGenre(Genre genre);

    Flux<Genre> getAllGenres();

    Mono<Long> countGenres();

    Mono<Void> deleteGenre(String id);

    Mono<Void> updateGenre(Genre genre);

    Mono<Genre> getGenreById(String genreId);
}
