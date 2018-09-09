package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.CommentRepository;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Comment;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.CommentService;
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

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {

    private final Author author = new Author("author_1_id", "Leo", "Tolstoy", RUSSIA);
    private final Genre genre = new Genre("genre_id", "novel");
    private final Book book = new Book("book_id", "War And Piece", new Date(), 1, Collections.emptyMap(), Collections.singleton(author), genre);
    private final Comment comment = new Comment("comment_id", "username", "comment", Collections.singleton(book));


    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    public void getAllComments_shouldReturnFluxOfTestComments() {
        when(commentRepository.findAll()).thenReturn(Flux.just(comment));

        Flux<Comment> allComments = commentService.getAllComments();

        StepVerifier
                .create(allComments)
                .expectSubscription()
                .expectNext(comment)
                .verifyComplete();

        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void countComments_shouldReturnTestCount() {
        when(commentRepository.count()).thenReturn(Mono.just(2L));

        Mono<Long> countBooks = commentService.countComments();

        StepVerifier
                .create(countBooks)
                .expectSubscription()
                .expectNext(2L)
                .verifyComplete();

        verify(commentRepository, times(1)).count();
    }

    @Test
    public void createComment_shouldCallCreateCommentAndReturnVoid() {
        when(commentRepository.save(eq(comment))).thenReturn(Mono.just(comment));

        Mono<Void> bookCreation = commentService.createComment(this.comment);

        StepVerifier
                .create(bookCreation)
                .expectSubscription()
                .verifyComplete();

        verify(commentRepository, times(1)).save(eq(comment));
    }

    @Test
    public void getCommentById_shouldReturnTestComment() {
        when(commentRepository.findById(eq(comment.getId()))).thenReturn(Mono.just(comment));

        Mono<Comment> commentById = commentService.getCommentById(comment.getId());

        StepVerifier
                .create(commentById)
                .expectSubscription()
                .expectNext(comment)
                .verifyComplete();

        verify(commentRepository, times(1)).findById(eq(comment.getId()));
    }

    @Test
    public void getCommentById_shouldThrowException_causeNoSuchComment() {
        when(commentRepository.findById(eq(comment.getId()))).thenReturn(Mono.empty());

        Mono<Comment> commentById = commentService.getCommentById(comment.getId());

        StepVerifier
                .create(commentById)
                .expectSubscription()
                .expectError(NotFoundException.class)
                .verify();

        verify(commentRepository, times(1)).findById(eq(comment.getId()));
    }

    @Test
    public void deleteComment_shouldCallDeleteCommentRepo() {
        when(commentRepository.deleteById(eq(comment.getId()))).thenReturn(Mono.empty());

        Mono<Void> commentDeleting = commentService.deleteComment(comment.getId());

        StepVerifier
                .create(commentDeleting)
                .expectSubscription()
                .verifyComplete();

        verify(commentRepository, times(1)).deleteById(eq(comment.getId()));
    }


}