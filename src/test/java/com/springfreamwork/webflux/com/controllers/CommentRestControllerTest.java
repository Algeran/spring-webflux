package com.springfreamwork.webflux.com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springfreamwork.webflux.domain.dto.CommentCreateDTO;
import com.springfreamwork.webflux.domain.dto.CommentDTO;
import com.springfreamwork.webflux.domain.dto.CommentDataDTO;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Comment;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.BookService;
import com.springfreamwork.webflux.domain.servicies.CommentService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebFluxTest(CommentRestController.class)
public class CommentRestControllerTest {

    private final Genre genre = new Genre("genre_id", "novel");
    private final Author author = new Author("author_id", "Leo", "Tolstoy", RUSSIA);
    private final Book book = new Book("book_id", "War And Piece", new Date(), 2, Collections.emptyMap(), Collections.singleton(author), genre);
    private final Comment comment = new Comment("comment_id", "username", "comment", Collections.singleton(book));
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private BookService bookService;
    @MockBean
    private CommentService commentService;

    @Test
    public void commentRestController_shouldReturnAllComments() throws Exception {
        when(commentService.getAllComments()).thenReturn(Flux.just(comment));

        String expected = mapper.writeValueAsString(Collections.singletonList(CommentDTO.getCommentDTO(comment)));

        webClient.get().uri("/getComments")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expected);

        verify(commentService, times(1)).getAllComments();
    }

    @Test
    public void commentRestController_shouldCallDeleteComment_andReturnSuccess() {
        when(commentService.deleteComment(eq(comment.getId()))).thenReturn(Mono.empty());

        webClient.delete().uri("/deleteComment?id={id}", comment.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(commentService, times(1)).deleteComment(eq(comment.getId()));
    }

    @Test
    public void commentRestController_shouldReturnDataWithExpectedCommentAndAllBooks() {
        when(bookService.getAllBooks()).thenReturn(Flux.just(book));
        when(commentService.getCommentById(eq(comment.getId()))).thenReturn(Mono.just(comment));

        CommentDataDTO expected = CommentDataDTO.getCommentDataDTO(comment, Collections.singletonList(book));

        webClient.get().uri("/getComment?id={id}", comment.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDataDTO.class)
                .isEqualTo(expected);

        verify(bookService, times(1)).getAllBooks();
        verify(commentService, times(1)).getCommentById(eq(comment.getId()));
    }

    @Test
    public void commentRestController_shouldReturnDataWithAllBooks() {
        when(bookService.getAllBooks()).thenReturn(Flux.just(book));

        CommentDataDTO expected = CommentDataDTO.getCommentDataDTO(null, Collections.singletonList(book));

        webClient.get().uri("/getCommentData")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDataDTO.class)
                .isEqualTo(expected);

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void commentRestController_shouldCallCreateComment_andReturnSuccess() {
        when(bookService.getBooksById(Collections.singleton(book.getId()))).thenReturn(Flux.just(book));
        when(commentService.createComment(eq(comment))).thenReturn(Mono.empty());

        CommentCreateDTO body = new CommentCreateDTO();
        body.setId(comment.getId());
        body.setUsername(comment.getUsername());
        body.setComment(comment.getComment());
        body.setBooks(Collections.singleton(book.getId()));

        webClient.post().uri("/createComment")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(body))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(bookService, times(1)).getBooksById(Collections.singleton(book.getId()));
        verify(commentService, times(1)).createComment(eq(comment));
    }

}