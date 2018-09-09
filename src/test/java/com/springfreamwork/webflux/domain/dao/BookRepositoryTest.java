package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Book;
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

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void bookRepositoryShouldGetBookById() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        mongoTemplate.save(author);
        mongoTemplate.save(genre);
        mongoTemplate.save(book);

        Mono<Book> bookFromRepo = bookRepository.findById(book.getId());

        StepVerifier
                .create(bookFromRepo)
                .expectSubscription()
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    public void bookRepositoryShouldGetBookByName() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        mongoTemplate.save(author);
        mongoTemplate.save(genre);
        mongoTemplate.save(book);

        Mono<Book> bookFromRepo = bookRepository.findByName(book.getName());

        StepVerifier
                .create(bookFromRepo)
                .expectSubscription()
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    public void bookRepositoryShouldGetAllBooks() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);
        Book book_2 = new Book("Anna Karenina", new Date(), parts, Collections.singleton(author), genre);

        mongoTemplate.save(author);
        mongoTemplate.save(genre);
        mongoTemplate.save(book);
        mongoTemplate.save(book_2);

        Flux<Book> books = bookRepository.findAll();

        StepVerifier
                .create(books)
                .expectSubscription()
                .expectNext(book)
                .expectNext(book_2)
                .verifyComplete();
    }

    @Test
    public void bookRepositoryShouldDeleteBookById() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);

        mongoTemplate.save(author);
        mongoTemplate.save(genre);
        mongoTemplate.save(book);

        bookRepository.deleteById(book.getId()).subscribe();

        Mono<Book> bookFromRepo = bookRepository.findById(book.getId());

        StepVerifier
                .create(bookFromRepo)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void bookRepositoryShouldReturnCount_2() {
        Map<Integer, String> parts = Collections.singletonMap(1, "partOne");
        Author author = new Author("Leo", "Tolstoy", RUSSIA);
        Genre genre = new Genre("novel");
        Book book = new Book("War And Piece", new Date(), parts, Collections.singleton(author), genre);
        Book book_2 = new Book("Anna Karenina", new Date(), parts, Collections.singleton(author), genre);

        long countBefore = bookRepository.count().block();

        mongoTemplate.save(author);
        mongoTemplate.save(genre);
        mongoTemplate.save(book);
        mongoTemplate.save(book_2);

        long count = bookRepository.count().block();

        assertThat(count - countBefore)
                .as("Checking counting books")
                .isEqualTo(2);
    }

}