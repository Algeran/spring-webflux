package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.EntityExistsException;
import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.BookRepository;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {

    private final Author author = new Author("author_1_id", "Leo", "Tolstoy", RUSSIA);
    private final Genre genre = new Genre("genre_id", "novel");
    private final Book book = new Book("book_id", "War And Piece", new Date(), 1, Collections.emptyMap(), Collections.singleton(author), genre);

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    public void getAllBooks_shouldReturnFluxOfTestBooks() {
        when(bookRepository.findAll()).thenReturn(Flux.just(book));

        Flux<Book> allBooks = bookService.getAllBooks();

        StepVerifier
                .create(allBooks)
                .expectSubscription()
                .expectNext(book)
                .verifyComplete();

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void countBooks_shouldReturnTestCount() {
        when(bookRepository.count()).thenReturn(Mono.just(2L));

        Mono<Long> countBooks = bookService.countBooks();

        StepVerifier
                .create(countBooks)
                .expectSubscription()
                .expectNext(2L)
                .verifyComplete();

        verify(bookRepository, times(1)).count();
    }

    @Test
    public void createBook_shouldCallCreateBookAndReturnVoid() {
        when(bookRepository.findByName(eq(book.getName()))).thenReturn(Mono.empty());
        when(bookRepository.save(eq(book))).thenReturn(Mono.just(book));

        Mono<Void> bookCreation = bookService.createBook(this.book);

        StepVerifier
                .create(bookCreation)
                .expectSubscription()
                .verifyComplete();

        verify(bookRepository, times(1)).findByName(eq(book.getName()));
        verify(bookRepository, times(1)).save(eq(book));
    }

    @Test
    public void createBook_shouldThrowException_causeBookAlreadyInDB() {
        when(bookRepository.findByName(eq(book.getName()))).thenReturn(Mono.just(book));
        when(bookRepository.save(eq(book))).thenReturn(Mono.just(book));

        Mono<Void> bookCreation = bookService.createBook(this.book);

        StepVerifier
                .create(bookCreation)
                .expectSubscription()
                .expectError(EntityExistsException.class)
                .verify();

        verify(bookRepository, times(1)).findByName(eq(book.getName()));
        verify(bookRepository, times(1)).save(eq(book));
    }

    @Test
    public void getBookById_shouldReturnTestBook() {
        when(bookRepository.findById(eq(book.getId()))).thenReturn(Mono.just(book));

        Mono<Book> bookById = bookService.getBook(book.getId());

        StepVerifier
                .create(bookById)
                .expectSubscription()
                .expectNext(book)
                .verifyComplete();

        verify(bookRepository, times(1)).findById(eq(book.getId()));
    }

    @Test
    public void getBookById_shouldThrowException_causeNoSuchBook() {
        when(bookRepository.findById(eq(book.getId()))).thenReturn(Mono.empty());

        Mono<Book> bookById = bookService.getBook(book.getId());

        StepVerifier
                .create(bookById)
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();

        verify(bookRepository, times(1)).findById(eq(book.getId()));
    }

    @Test
    public void getBooksById_shouldReturnTestBooks() {
        Set<String> bookIds = Collections.singleton(book.getId());
        when(bookRepository.findAllById(bookIds)).thenReturn(Flux.just(book));

        Flux<Book> books = bookService.getBooksById(bookIds);

        StepVerifier
                .create(books)
                .expectSubscription()
                .expectNext(book)
                .verifyComplete();

        verify(bookRepository, times(1)).findAllById(bookIds);
    }

    @Test
    public void updateBook_shouldCallSaveBookRepo() {
        when(bookRepository.save(eq(book))).thenReturn(Mono.just(book));

        Mono<Void> bookUpdating = bookService.updateBook(book);

        StepVerifier
                .create(bookUpdating)
                .expectSubscription()
                .verifyComplete();

        verify(bookRepository, times(1)).save(eq(book));
    }

    @Test
    public void deleteBook_shouldCallDeleteBookRepo() {
        when(bookRepository.deleteById(eq(book.getId()))).thenReturn(Mono.empty());

        Mono<Void> bookDeleting = bookService.deleteBook(book.getId());

        StepVerifier
                .create(bookDeleting)
                .expectSubscription()
                .verifyComplete();

        verify(bookRepository, times(1)).deleteById(eq(book.getId()));
    }

}