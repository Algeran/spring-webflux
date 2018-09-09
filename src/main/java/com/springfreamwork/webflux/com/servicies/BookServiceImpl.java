package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.EntityExistsException;
import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.BookRepository;
import com.springfreamwork.webflux.domain.model.Book;
import com.springfreamwork.webflux.domain.servicies.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(
            BookRepository bookRepository
    ) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Flux<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Mono<Long> countBooks() {
        return bookRepository.count();
    }

    @Override
    public Mono<Void> deleteBook(String id) {
        return bookRepository.deleteById(id);
    }

    @Override
    public Mono<Book> getBook(String id) {
        return bookRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Не найдено книги в базе")));
    }

    @Override
    public Mono<Void> updateBook(Book book) {
        return Mono.just(book)
                .flatMap(bookRepository::save)
                .then();
    }

    @Override
    public Mono<Void> createBook(Book book) {
        return bookRepository.findByName(book.getName())
                .flatMap(b -> b != null ? Mono.error(new EntityExistsException("Книга с таким наименованием уже в базе")) : Mono.empty())
                .switchIfEmpty(bookRepository.save(book))
                .then();
    }

    @Override
    public Flux<Book> getBooksById(Set<String> ids) {
        return bookRepository.findAllById(ids);
    }
}
