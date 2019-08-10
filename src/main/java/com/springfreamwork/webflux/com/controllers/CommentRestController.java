package com.springfreamwork.webflux.com.controllers;

import com.springfreamwork.webflux.domain.dto.CommentCreateDTO;
import com.springfreamwork.webflux.domain.dto.CommentDTO;
import com.springfreamwork.webflux.domain.dto.CommentDataDTO;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Comment;
import com.springfreamwork.webflux.domain.servicies.BookService;
import com.springfreamwork.webflux.domain.servicies.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentRestController {

    private final CommentService commentService;
    private final BookService bookService;

    @Autowired
    public CommentRestController(
            CommentService commentService,
            BookService bookService
    ) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    @GetMapping("/getComments")
    public Flux<CommentDTO> getAllComments() {
        return commentService.getAllComments()
                .map(CommentDTO::getCommentDTO);
    }


    @DeleteMapping("/deleteComment")
    public Mono<String> deleteComment(
            @RequestParam("id") String id
    ) {
        return commentService.deleteComment(id)
                .then(Mono.just("success"));
    }

    @GetMapping("/getComment")
    public Mono<CommentDataDTO> getCommentData(@RequestParam String id) {
        Mono<List<Book>> allBooks = bookService.getAllBooks().collectList();
        return commentService.getCommentById(id)
                .map(comment -> CommentDataDTO.getCommentDataDTO(comment, null))
                .flatMap(commentDataDTO ->
                        allBooks.map(booksList -> {
                            commentDataDTO.setAllBooks(booksList);
                            return commentDataDTO;
                        })
                );
    }

    @GetMapping("/getCommentData")
    public Mono<CommentDataDTO> createBookPage() {
        return bookService.getAllBooks().collectList()
                .map(allBooks -> CommentDataDTO.getCommentDataDTO(null, allBooks));
    }

    @PostMapping("/createComment")
    public Mono<String> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        Mono<Set<Book>> books = bookService.getBooksById(commentCreateDTO.getBooks()).collect(Collectors.toSet());
        return Mono.just(CommentCreateDTO.getComment(commentCreateDTO))
                .flatMap(comment ->
                        books.map(booksSet -> {
                            comment.setBooks(booksSet);
                            return comment;
                        })
                ).flatMap(commentService::createComment)
                .then(Mono.just("success"));
    }
}
