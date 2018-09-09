package com.springfreamwork.webflux.domain.dao;

import com.springfreamwork.webflux.domain.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CommentRepository extends ReactiveMongoRepository<Comment,String> {

}
