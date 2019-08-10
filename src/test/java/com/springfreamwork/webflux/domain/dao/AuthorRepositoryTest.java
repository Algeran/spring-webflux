package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Author;
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

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void authorRepositoryShouldGetAuthorByNameAndSurname() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        mongoTemplate.save(author);

        Mono<Author> authorFromRepo = authorRepository.findByNameAndSurname("Leo", "Tolstoy");

        StepVerifier
                .create(authorFromRepo)
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    public void authorRepositoryShouldGetAllAuthors() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Author author_2 = new Author("Fyodor", "Dostoevsky", RUSSIA);

        mongoTemplate.save(author);
        mongoTemplate.save(author_2);

        Flux<Author> authors = authorRepository.findAll();

        StepVerifier
                .create(authors)
                .expectSubscription()
                .expectNext(author)
                .expectNext(author_2)
                .verifyComplete();
    }

    @Test
    public void authorRepositoryShouldDeleteAuthorById() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);

        mongoTemplate.save(author);

        authorRepository.deleteById(author.getId()).subscribe();

        Mono<Author> authorFromRepo = authorRepository.findById(author.getId());

        StepVerifier
                .create(authorFromRepo)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void authorRepositoryShouldReturnCount_2() {
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Author author_2 = new Author("Fyodor", "Dostoevsky", RUSSIA);

        long countBefore = authorRepository.count().block();

        mongoTemplate.save(author);
        mongoTemplate.save(author_2);

        long count = authorRepository.count().block();

        assertThat(count - countBefore)
                .as("Checking counting authors")
                .isEqualTo(2);
    }

}