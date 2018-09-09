package com.springfreamwork.webflux.domain.servicies;

import com.springfreamwork.webflux.domain.model.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentService {

    Flux<Comment> getAllComments();

    Mono<Long> countComments();

    Mono<Comment> getCommentById(String id);

    Mono<Void> createComment(Comment comment);

    Mono<Void> deleteComment(String id);
}
