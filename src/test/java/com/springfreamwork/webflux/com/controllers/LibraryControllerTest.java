package com.springfreamwork.webflux.com.controllers;

import com.springfreamwork.webflux.domain.dto.LibraryDTO;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import com.springfreamwork.webflux.domain.servicies.BookService;
import com.springfreamwork.webflux.domain.servicies.CommentService;
import com.springfreamwork.webflux.domain.servicies.GenreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(LibraryController.class)
public class LibraryControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @Test
    public void libraryController_shouldReturnDataOfCountObjects() {
        when(genreService.countGenres()).thenReturn(Mono.just(1L));
        when(bookService.countBooks()).thenReturn(Mono.just(2L));
        when(authorService.countAuthors()).thenReturn(Mono.just(3L));
        when(commentService.countComments()).thenReturn(Mono.just(4L));

        LibraryDTO expected = new LibraryDTO();
        expected.setGenreCount(1L);
        expected.setBookCount(2L);
        expected.setAuthorCount(3L);
        expected.setCommentCount(4L);

        webClient.get().uri("/countObjects")
                .exchange()
                .expectStatus().isOk()
                .expectBody(LibraryDTO.class)
                .isEqualTo(expected);
    }
}