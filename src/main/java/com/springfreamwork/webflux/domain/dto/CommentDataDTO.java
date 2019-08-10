package com.springfreamwork.webflux.domain.dto;


import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Comment;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CommentDataDTO {

    private String id;
    private String username;
    private String comment;
    private Set<Book> books;
    private List<Book> allBooks;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public List<Book> getAllBooks() {
        return allBooks;
    }

    public void setAllBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }

    public static CommentDataDTO getCommentDataDTO(Comment comment, List<Book> allBooks) {
        CommentDataDTO commentDataDTO = new CommentDataDTO();
        if (comment != null) {
            commentDataDTO.setId(comment.getId());
            commentDataDTO.setUsername(comment.getUsername());
            commentDataDTO.setComment(comment.getComment());
            commentDataDTO.setBooks(comment.getBooks());
        }
        commentDataDTO.setAllBooks(allBooks);
        return commentDataDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDataDTO)) return false;
        CommentDataDTO that = (CommentDataDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getComment(), that.getComment()) &&
                Objects.equals(getBooks(), that.getBooks()) &&
                Objects.equals(getAllBooks(), that.getAllBooks());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getUsername(), getComment(), getBooks(), getAllBooks());
    }
}
