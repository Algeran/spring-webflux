package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Genre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class GenreRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void genreRepositoryShouldGetGenreById() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);

        Mono<Genre> genreFromRepo = genreRepository.findById(genre.getId());

        StepVerifier
                .create(genreFromRepo)
                .expectSubscription()
                .expectNext(genre)
                .verifyComplete();
    }

    @Test
    public void genreRepositoryShouldGetGenreByName() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);

        Mono<Genre> genreFromRepo = genreRepository.findByName(genre.getName());

        StepVerifier
                .create(genreFromRepo)
                .expectSubscription()
                .expectNext(genre)
                .verifyComplete();
    }

    @Test
    public void genreRepositoryShouldGetAllGenres() {
        Genre genre = new Genre("fantasy");
        Genre genre_2 = new Genre("novel");

        mongoTemplate.save(genre);
        mongoTemplate.save(genre_2);

        Flux<Genre> genresFromRepo = genreRepository.findAll();

        StepVerifier
                .create(genresFromRepo)
                .expectSubscription()
                .expectNext(genre)
                .expectNext(genre_2)
                .verifyComplete();
    }

    @Test
    public void genreRepositoryShouldDeleteGenreById() {
        Genre genre = new Genre("fantasy");

        mongoTemplate.save(genre);
        String id = genre.getId();

        genreRepository.deleteById(id).subscribe();

        Mono<Genre> genreFromRepo = genreRepository.findById(id);

        StepVerifier
                .create(genreFromRepo)
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    public void genreRepositoryShouldReturnCount_2() {
        Genre genre = new Genre("fantasy");
        Genre genre_2 = new Genre("novel");

        long countBefore = genreRepository.count().block();

        mongoTemplate.save(genre);
        mongoTemplate.save(genre_2);

        long count = genreRepository.count().block();

        assertThat(count - countBefore)
                .as("Checking counting genres")
                .isEqualTo(2);
    }

}