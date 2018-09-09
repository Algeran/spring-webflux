package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Comment;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void commentRepositoryShouldGetCommentById() {
        Comment comment = new Comment("user", "so good");

        mongoTemplate.save(comment);

        Mono<Comment> commentFromRepo = commentRepository.findById(comment.getId());

        StepVerifier
                .create(commentFromRepo)
                .expectSubscription()
                .expectNext(comment)
                .verifyComplete();
    }

    @Test
    public void commentRepositoryShouldGetAllComments() {
        Comment comment = new Comment("user", "so good");
        Comment comment_2 = new Comment("user2", "so bad");

        mongoTemplate.save(comment);
        mongoTemplate.save(comment_2);

        Flux<Comment> comments = commentRepository.findAll();

        StepVerifier
                .create(comments)
                .expectSubscription()
                .expectNext(comment)
                .expectNext(comment_2)
                .verifyComplete();
    }

    @Test
    public void commentRepositoryShouldDeleteCommentById() {
        Comment comment = new Comment("user", "so good");

        mongoTemplate.save(comment);

        commentRepository.deleteById(comment.getId()).subscribe();

        Mono<Comment> commentFromRepo = commentRepository.findById(comment.getId());

        StepVerifier
                .create(commentFromRepo)
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void commentRepositoryShouldReturnCount_2() {
        Comment comment = new Comment("user", "so good");
        Comment comment_2 = new Comment("user2", "so bad");

        long countBefore = commentRepository.count().block();

        mongoTemplate.save(comment);
        mongoTemplate.save(comment_2);

        long count = commentRepository.count().block();

        assertThat(count - countBefore)
                .as("Checking counting all comments")
                .isEqualTo(2);
    }

}