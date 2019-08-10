package com.springfreamwork.webflux.com.controllers;

import com.springfreamwork.webflux.domain.dto.LibraryDTO;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import com.springfreamwork.webflux.domain.servicies.BookService;
import com.springfreamwork.webflux.domain.servicies.CommentService;
import com.springfreamwork.webflux.domain.servicies.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LibraryController {

    private final GenreService genreService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final CommentService commentService;

    @Autowired
    public LibraryController(
            GenreService genreService,
            AuthorService authorService,
            BookService bookService,
            CommentService commentService
    ) {
        this.genreService = genreService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.commentService = commentService;
    }

    @GetMapping("/countObjects")
    public Mono<LibraryDTO> welcomePage() {
        Mono<Long> countGenres = genreService.countGenres();
        Mono<Long> countAuthors = authorService.countAuthors();
        Mono<Long> countBooks = bookService.countBooks();
        Mono<Long> countComments = commentService.countComments();
        return Mono.just(new LibraryDTO())
                .flatMap(libraryDTO ->
                        countGenres.map(g -> {
                            libraryDTO.setGenreCount(g);
                            return libraryDTO;
                        }))
                .flatMap(libraryDTO ->
                        countAuthors.map(a -> {
                            libraryDTO.setAuthorCount(a);
                            return libraryDTO;
                        }))
                .flatMap(libraryDTO ->
                        countBooks.map(b -> {
                            libraryDTO.setBookCount(b);
                            return libraryDTO;
                        }))
                .flatMap(libraryDTO ->
                        countComments.map(c -> {
                            libraryDTO.setCommentCount(c);
                            return libraryDTO;
                        }));
    }
}
