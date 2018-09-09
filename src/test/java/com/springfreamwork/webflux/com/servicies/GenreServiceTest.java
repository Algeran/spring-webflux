package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.EntityExistsException;
import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.GenreRepository;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.GenreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenreServiceTest {

    private final Genre genre = new Genre("genre_id", "novel");

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;


    @Test
    public void getAllGenres_shouldReturnFluxOfTestGenres() {
        when(genreRepository.findAll()).thenReturn(Flux.just(genre));

        Flux<Genre> allGenres = genreService.getAllGenres();

        StepVerifier
                .create(allGenres)
                .expectSubscription()
                .expectNext(genre)
                .verifyComplete();

        verify(genreRepository, times(1)).findAll();
    }

    @Test
    public void countGenres_shouldReturnTestCount() {
        when(genreRepository.count()).thenReturn(Mono.just(2L));

        Mono<Long> countAuthors = genreService.countGenres();

        StepVerifier
                .create(countAuthors)
                .expectSubscription()
                .expectNext(2L)
                .verifyComplete();

        verify(genreRepository, times(1)).count();
    }

    @Test
    public void createGenre_shouldCallCreateGenreAndReturnVoid() {
        when(genreRepository.findByName(eq(genre.getName()))).thenReturn(Mono.empty());
        when(genreRepository.save(eq(genre))).thenReturn(Mono.just(genre));

        Mono<Void> genreCreation = genreService.createGenre(this.genre);

        StepVerifier
                .create(genreCreation)
                .expectSubscription()
                .verifyComplete();

        verify(genreRepository, times(1)).findByName(eq(genre.getName()));
        verify(genreRepository, times(1)).save(eq(genre));
    }

    @Test
    public void createGenre_shouldThrowException_causeGenreAlreadyInDB() {
        when(genreRepository.findByName(eq(genre.getName()))).thenReturn(Mono.just(genre));
        when(genreRepository.save(eq(genre))).thenReturn(Mono.just(genre));

        Mono<Void> genreCreation = genreService.createGenre(this.genre);

        StepVerifier
                .create(genreCreation)
                .expectSubscription()
                .expectError(EntityExistsException.class)
                .verify();

        verify(genreRepository, times(1)).findByName(eq(genre.getName()));
        verify(genreRepository, times(1)).save(eq(genre));
    }

    @Test
    public void getGenreById_shouldReturnTestGenre() {
        when(genreRepository.findById(eq(genre.getId()))).thenReturn(Mono.just(genre));

        Mono<Genre> genreById = genreService.getGenreById(genre.getId());

        StepVerifier
                .create(genreById)
                .expectSubscription()
                .expectNext(genre)
                .verifyComplete();

        verify(genreRepository, times(1)).findById(eq(genre.getId()));
    }

    @Test
    public void getGenreById_shouldThrowException_causeNoSuchGenre() {
        when(genreRepository.findById(eq(genre.getId()))).thenReturn(Mono.empty());

        Mono<Genre> genreById = genreService.getGenreById(genre.getId());

        StepVerifier
                .create(genreById)
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();

        verify(genreRepository, times(1)).findById(eq(genre.getId()));
    }


    @Test
    public void updateGenre_shouldCallSaveGenreRepo() {
        when(genreRepository.findById(eq(genre.getId()))).thenReturn(Mono.just(genre));
        when(genreRepository.save(eq(genre))).thenReturn(Mono.just(genre));

        Mono<Void> genreUpdating = genreService.updateGenre(genre);

        StepVerifier
                .create(genreUpdating)
                .expectSubscription()
                .verifyComplete();

        verify(genreRepository, times(1)).findById(eq(genre.getId()));
        verify(genreRepository, times(1)).save(eq(genre));
    }

    @Test
    public void updateGenre_shouldThrowException_causeNoSuchGenreInDB() {
        when(genreRepository.findById(eq(genre.getId()))).thenReturn(Mono.empty());
        when(genreRepository.save(eq(genre))).thenReturn(Mono.just(genre));

        Mono<Void> genreUpdating = genreService.updateGenre(genre);

        StepVerifier
                .create(genreUpdating)
                .expectError(NotFoundException.class)
                .verify();

        verify(genreRepository, times(1)).findById(eq(genre.getId()));
    }

    @Test
    public void deleteGenre_shouldCallDeleteGenreRepo() {
        when(genreRepository.deleteById(eq(genre.getId()))).thenReturn(Mono.empty());

        Mono<Void> genreDeleting = genreService.deleteGenre(genre.getId());

        StepVerifier
                .create(genreDeleting)
                .expectSubscription()
                .verifyComplete();

        verify(genreRepository, times(1)).deleteById(eq(genre.getId()));
    }

}