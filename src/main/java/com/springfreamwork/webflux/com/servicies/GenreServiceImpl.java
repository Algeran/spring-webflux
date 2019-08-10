package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.EntityExistsException;
import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.GenreRepository;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Mono<Void> createGenre(Genre genre) {
        return genreRepository.findByName(genre.getName())
                .flatMap(g -> g != null
                        ? Mono.error(new EntityExistsException("Жанр с таким именем уже существует"))
                        : Mono.empty())
                .switchIfEmpty(genreRepository.save(genre))
                .then();
    }

    @Override
    public Flux<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Mono<Long> countGenres() {
        return genreRepository.count();
    }

    @Override
    public Mono<Void> deleteGenre(String id) {
        return genreRepository.deleteById(id);
    }

    @Override
    public Mono<Void> updateGenre(Genre genre) {
        return genreRepository.findById(genre.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("Не найден жанр для обновления")))
                .flatMap(g -> genreRepository.save(genre)).then();
    }

    @Override
    public Mono<Genre> getGenreById(String genreId) {
        return genreRepository.findById(genreId)
                .switchIfEmpty(Mono.error(new NotFoundException("Не найдено жанра с id " + genreId)));
    }
}
