package com.springfreamwork.webflux.com.controllers;

import com.springfreamwork.webflux.domain.dto.BookCreateDTO;
import com.springfreamwork.webflux.domain.dto.BookDTO;
import com.springfreamwork.webflux.domain.dto.BookDataDTO;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import com.springfreamwork.webflux.domain.servicies.BookService;
import com.springfreamwork.webflux.domain.servicies.GenreService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class BookRestController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookRestController(
            BookService bookService,
            AuthorService authorService,
            GenreService genreService
    ) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @DeleteMapping("/deleteBook")
    public Mono<String> deleteBook(@RequestParam("id") String id) {
        return bookService.deleteBook(id)
                .then(Mono.just("success"));
    }

    @GetMapping("/getBooks")
    public Flux<BookDTO> getAllBooks() {
        return bookService.getAllBooks().map(BookDTO::getBookDTO);
    }

    @GetMapping("/getBook")
    public Mono<BookDataDTO> editBookPage(
            @RequestParam("id") String id
    ) {
        Mono<List<Author>> authors = authorService.getAllAuthors().collectList();
        Mono<List<Genre>> genres = genreService.getAllGenres().collectList();

        return bookService.getBook(id)
                .map(book -> BookDataDTO.getBookDataDTO(book, null, null))
                .flatMap(bDTO ->
                        authors.map(authorsList -> {
                            bDTO.setAllAuthors(authorsList);
                            return bDTO;
                        })
                ).flatMap(bDTO ->
                        genres.map(genresList -> {
                            bDTO.setAllGenres(genresList);
                            return bDTO;
                        })
                );
    }

    @PostMapping("/editBook")
    public Mono<String> editBook(
            @RequestBody BookCreateDTO bookCreateDTO
    ) {
        Mono<Set<Author>> authors = authorService.getAuthorsById(bookCreateDTO.getAuthors()).collect(Collectors.toSet());
        Mono<Genre> genre = genreService.getGenreById(bookCreateDTO.getGenre());
        return Mono.just(BookCreateDTO.getBook(bookCreateDTO))
                .flatMap(book ->
                        authors.map(authorsList -> {
                            book.setAuthors(authorsList);
                            return book;
                        })
                ).flatMap(book ->
                        genre.map(g -> {
                            book.setGenre(g);
                            return book;
                        })
                ).flatMap(bookService::updateBook)
                .then(Mono.just("success"));
    }

    @GetMapping("/getBookData")
    public Mono<BookDataDTO> createBookPage() {
        Mono<List<Author>> authors = authorService.getAllAuthors().collectList();
        Mono<List<Genre>> genres = genreService.getAllGenres().collectList();
        return Mono.just(new BookDataDTO())
                .flatMap(bookDataDTO ->
                        authors.map(authorsList -> {
                            bookDataDTO.setAllAuthors(authorsList);
                            return bookDataDTO;
                        })
                ).flatMap(bookDataDTO ->
                        genres.map(genresList -> {
                            bookDataDTO.setAllGenres(genresList);
                            return bookDataDTO;
                        })
                );
    }

    @PostMapping(value = "/createBook")
    public Mono<String> createBook(@RequestBody BookCreateDTO bookCreateDTO) {
        Mono<Set<Author>> authors = authorService.getAuthorsById(bookCreateDTO.getAuthors()).collect(Collectors.toSet());
        Mono<Genre> genre = genreService.getGenreById(bookCreateDTO.getGenre());
        return Mono.just(BookCreateDTO.getBook(bookCreateDTO))
                .flatMap(book ->
                        authors.map(authorsList -> {
                            book.setAuthors(authorsList);
                            return book;
                        })
                ).flatMap(book ->
                        genre.map(g -> {
                            book.setGenre(g);
                            return book;
                        })
                ).flatMap(bookService::createBook)
                .then(Mono.just("success"));
    }
}
