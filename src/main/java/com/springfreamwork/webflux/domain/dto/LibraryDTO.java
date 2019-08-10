package com.springfreamwork.webflux.domain.dto;

import java.util.Objects;

public class LibraryDTO {

    private long genreCount;
    private long authorCount;
    private long bookCount;
    private long commentCount;

    public LibraryDTO(){}

    public long getGenreCount() {
        return genreCount;
    }

    public void setGenreCount(long genreCount) {
        this.genreCount = genreCount;
    }

    public long getAuthorCount() {
        return authorCount;
    }

    public void setAuthorCount(long authorCount) {
        this.authorCount = authorCount;
    }

    public long getBookCount() {
        return bookCount;
    }

    public void setBookCount(long bookCount) {
        this.bookCount = bookCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibraryDTO)) return false;
        LibraryDTO that = (LibraryDTO) o;
        return getGenreCount() == that.getGenreCount() &&
                getAuthorCount() == that.getAuthorCount() &&
                getBookCount() == that.getBookCount() &&
                getCommentCount() == that.getCommentCount();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getGenreCount(), getAuthorCount(), getBookCount(), getCommentCount());
    }
}
