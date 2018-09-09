package com.springfreamwork.webflux.domain.dto;


import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.model.Genre;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class BookDataDTO {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String id;
    private String name;
    private String date;
    private Set<Author> authors;
    private Genre genre;
    private List<Author> allAuthors;
    private List<Genre> allGenres;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Author> getAllAuthors() {
        return allAuthors;
    }

    public void setAllAuthors(List<Author> allAuthors) {
        this.allAuthors = allAuthors;
    }

    public List<Genre> getAllGenres() {
        return allGenres;
    }

    public void setAllGenres(List<Genre> allGenres) {
        this.allGenres = allGenres;
    }

    public static BookDataDTO getBookDataDTO(Book book, List<Author> authors, List<Genre> genres) {
        BookDataDTO bookDataDTO = new BookDataDTO();
        if (book != null) {
            bookDataDTO.setId(book.getId());
            bookDataDTO.setName(book.getName());
            bookDataDTO.setDate(dateFormat.format(book.getPublishedDate()));
            bookDataDTO.setAuthors(book.getAuthors());
            bookDataDTO.setGenre(book.getGenre());
        }
        bookDataDTO.setAllAuthors(authors);
        bookDataDTO.setAllGenres(genres);
        return bookDataDTO;
    }

    @Override
    public String toString() {
        return "BookDataDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", authors=" + authors +
                ", genre=" + genre +
                ", allAuthors=" + allAuthors +
                ", allGenres=" + allGenres +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDataDTO)) return false;
        BookDataDTO that = (BookDataDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getAuthors(), that.getAuthors()) &&
                Objects.equals(getGenre(), that.getGenre()) &&
                Objects.equals(getAllAuthors(), that.getAllAuthors()) &&
                Objects.equals(getAllGenres(), that.getAllGenres());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getName(), getDate(), getAuthors(), getGenre(), getAllAuthors(), getAllGenres());
    }
}
