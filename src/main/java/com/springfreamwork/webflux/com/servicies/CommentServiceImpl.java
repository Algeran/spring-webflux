package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.CommentRepository;
import com.springfreamwork.webflux.domain.model.Comment;
import com.springfreamwork.webflux.domain.servicies.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(
            CommentRepository commentRepository
    ) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Flux<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Mono<Long> countComments() {
        return commentRepository.count();
    }

    @Override
    public Mono<Comment> getCommentById(String id) {
        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Не найдено комментария")));
    }

    @Override
    public Mono<Void> createComment(Comment comment) {
        return commentRepository.save(comment)
                .then();
    }

    @Override
    public Mono<Void> deleteComment(String id) {
        return commentRepository.deleteById(id);
    }

}
