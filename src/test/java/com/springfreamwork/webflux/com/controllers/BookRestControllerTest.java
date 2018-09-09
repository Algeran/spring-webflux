package com.springfreamwork.webflux.com.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springfreamwork.webflux.domain.dto.BookCreateDTO;
import com.springfreamwork.webflux.domain.dto.BookDTO;
import com.springfreamwork.webflux.domain.dto.BookDataDTO;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import com.springfreamwork.webflux.domain.servicies.BookService;
import com.springfreamwork.webflux.domain.servicies.GenreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(BookRestController.class)
public class BookRestControllerTest {

    private final Genre genre = new Genre("genre_id", "novel");
    private final Author author = new Author("author_id", "Leo", "Tolstoy", RUSSIA);
    private final Book book = new Book("book_id", "War And Piece", new Date(), 0, null, Collections.singleton(author), genre);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @Test
    public void bookRestController_shouldCallDeleteBook_andReturnSuccess() {
        when(bookService.deleteBook(eq(book.getId()))).thenReturn(Mono.empty());

        webClient.delete().uri("/deleteBook?id={id}", book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(bookService, times(1)).deleteBook(eq(book.getId()));
    }

    @Test
    public void bookRestController_shouldReturnAllBooksData() throws JsonProcessingException {
        when(bookService.getAllBooks()).thenReturn(Flux.just(book));

        String expected = mapper.writeValueAsString(Collections.singletonList(BookDTO.getBookDTO(book)));

        webClient.get().uri("/getBooks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expected);

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void bookRestController_shouldReturnBookDataWithAllAuthorsAndAllGenres() {
        when(genreService.getAllGenres()).thenReturn(Flux.just(genre));
        when(authorService.getAllAuthors()).thenReturn(Flux.just(author));
        when(bookService.getBook(eq(book.getId()))).thenReturn(Mono.just(book));

        BookDataDTO expected = BookDataDTO.getBookDataDTO(book, Collections.singletonList(author), Collections.singletonList(genre));

        webClient.get().uri("/getBook?id={id}", book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDataDTO.class)
                .isEqualTo(expected);

        verify(genreService, times(1)).getAllGenres();
        verify(authorService, times(1)).getAllAuthors();
        verify(bookService, times(1)).getBook(eq(book.getId()));
    }

    @Test
    public void bookRestController_shouldCallUpdateBook_andReturnSuccess() {
        when(authorService.getAuthorsById(eq(Collections.singleton(author.getId())))).thenReturn(Flux.just(author));
        when(genreService.getGenreById(eq(genre.getId()))).thenReturn(Mono.just(genre));
        when(bookService.updateBook(any())).thenReturn(Mono.empty());

        BookCreateDTO body = new BookCreateDTO();
        body.setId(book.getId());
        body.setDate(book.getPublishedDate());
        body.setName(book.getName());
        body.setAuthors(Collections.singleton(author.getId()));
        body.setGenre(genre.getId());

        webClient.post().uri("/editBook")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(authorService, times(1)).getAuthorsById(eq(Collections.singleton(author.getId())));
        verify(genreService, times(1)).getGenreById(eq(genre.getId()));
    }

    @Test
    public void bookRestController_shouldReturnDataWithAllAuthorsAndAllGenres() {
        when(genreService.getAllGenres()).thenReturn(Flux.just(genre));
        when(authorService.getAllAuthors()).thenReturn(Flux.just(author));

        BookDataDTO expected = BookDataDTO.getBookDataDTO(null, Collections.singletonList(author), Collections.singletonList(genre));

        webClient.get().uri("/getBookData")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDataDTO.class)
                .isEqualTo(expected);

        verify(genreService, times(1)).getAllGenres();
        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    public void bookRestController_shouldCallCreateBook_andReturnSuccess() {
        when(authorService.getAuthorsById(eq(Collections.singleton(author.getId())))).thenReturn(Flux.just(author));
        when(genreService.getGenreById(eq(genre.getId()))).thenReturn(Mono.just(genre));
        when(bookService.createBook(any())).thenReturn(Mono.empty());

        BookCreateDTO body = new BookCreateDTO();
        body.setId(book.getId());
        body.setDate(book.getPublishedDate());
        body.setName(book.getName());
        body.setAuthors(Collections.singleton(author.getId()));
        body.setGenre(genre.getId());

        webClient.post().uri("/createBook")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(authorService, times(1)).getAuthorsById(eq(Collections.singleton(author.getId())));
        verify(genreService, times(1)).getGenreById(eq(genre.getId()));
    }

}