package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.EntityExistsException;
import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.AuthorRepository;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
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

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static com.springfreamwork.webflux.domain.model.Country.USA;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthorServiceTest {

    private final Author author = new Author("author_1_id", "Leo", "Tolstoy", RUSSIA);
    private final Author author_2 = new Author("author_2_id", "Some", "Author", USA);

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;


    @Test
    public void getAllAuthors_shouldReturnFluxOfTestAuthors() {
        when(authorRepository.findAll()).thenReturn(Flux.just(author));

        Flux<Author> allAuthors = authorService.getAllAuthors();

        StepVerifier
                .create(allAuthors)
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void countAuthors_shouldReturnTestCount() {
        when(authorRepository.count()).thenReturn(Mono.just(2L));

        Mono<Long> countAuthors = authorService.countAuthors();

        StepVerifier
                .create(countAuthors)
                .expectSubscription()
                .expectNext(2L)
                .verifyComplete();

        verify(authorRepository, times(1)).count();
    }

    @Test
    public void createAuthor_shouldCallCreateAuthorAndReturnVoid() {
        when(authorRepository.findByNameAndSurname(eq(author.getName()), eq(author.getSurname()))).thenReturn(Mono.empty());
        when(authorRepository.save(eq(author))).thenReturn(Mono.just(author));

        Mono<Void> authorCreation = authorService.createAuthor(this.author);

        StepVerifier
                .create(authorCreation)
                .expectSubscription()
                .verifyComplete();

        verify(authorRepository, times(1)).findByNameAndSurname(eq(author.getName()), eq(author.getSurname()));
        verify(authorRepository, times(1)).save(eq(author));
    }

    @Test
    public void createAuthor_shouldThrowException_causeAuthorAlreadyInDB() {
        when(authorRepository.findByNameAndSurname(eq(author.getName()), eq(author.getSurname()))).thenReturn(Mono.just(author));
        when(authorRepository.save(eq(author))).thenReturn(Mono.just(author));

        Mono<Void> authorsCreation = authorService.createAuthor(this.author);

        StepVerifier
                .create(authorsCreation)
                .expectSubscription()
                .expectError(EntityExistsException.class)
                .verify();

        verify(authorRepository, times(1)).findByNameAndSurname(eq(author.getName()), eq(author.getSurname()));
        verify(authorRepository, times(1)).save(eq(author));
    }

    @Test
    public void getAuthorById_shouldReturnTestAuthor() {
        when(authorRepository.findById(eq(author.getId()))).thenReturn(Mono.just(author));

        Mono<Author> authorById = authorService.getAuthorById(author.getId());

        StepVerifier
                .create(authorById)
                .expectSubscription()
                .expectNext(author)
                .verifyComplete();

        verify(authorRepository, times(1)).findById(eq(author.getId()));
    }

    @Test
    public void getAuthorById_shouldThrowException_causeNoSuchAuthor() {
        when(authorRepository.findById(eq(author.getId()))).thenReturn(Mono.empty());

        Mono<Author> authorById = authorService.getAuthorById(author.getId());

        StepVerifier
                .create(authorById)
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();

        verify(authorRepository, times(1)).findById(eq(author.getId()));
    }

    @Test
    public void getAuthorsById_shouldReturnTestAuthors() {
        Set<String> authorIds = new HashSet<>(Arrays.asList(author.getId(), author_2.getId()));
        when(authorRepository.findAllById(authorIds)).thenReturn(Flux.just(author, author_2));

        Flux<Author> authors = authorService.getAuthorsById(authorIds);

        StepVerifier
                .create(authors)
                .expectSubscription()
                .expectNext(author)
                .expectNext(author_2)
                .verifyComplete();

        verify(authorRepository, times(1)).findAllById(authorIds);
    }

    @Test
    public void updateAuthor_shouldCallSaveAuthorRepo() {
        when(authorRepository.save(eq(author))).thenReturn(Mono.just(author));

        Mono<Void> authorUpdating = authorService.updateAuthor(author);

        StepVerifier
                .create(authorUpdating)
                .expectSubscription()
                .verifyComplete();

        verify(authorRepository, times(1)).save(eq(author));
    }

    @Test
    public void deleteAuthor_shouldCallDeleteAuthorRepo() {
        when(authorRepository.deleteById(eq(author.getId()))).thenReturn(Mono.empty());

        Mono<Void> authorDeleting = authorService.deleteAuthor(author.getId());

        StepVerifier
                .create(authorDeleting)
                .expectSubscription()
                .verifyComplete();

        verify(authorRepository, times(1)).deleteById(eq(author.getId()));
    }

}