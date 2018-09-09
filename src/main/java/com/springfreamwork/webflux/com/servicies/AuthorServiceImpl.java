package com.springfreamwork.webflux.com.servicies;

import com.springfreamwork.webflux.com.utility.EntityExistsException;
import com.springfreamwork.webflux.com.utility.NotFoundException;
import com.springfreamwork.webflux.domain.dao.AuthorRepository;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Flux<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Mono<Long> countAuthors() {
        return authorRepository.count();
    }

    @Override
    public Mono<Void> createAuthor(Author author) {
        return authorRepository.findByNameAndSurname(author.getName(), author.getSurname())
                .flatMap(a -> a != null ? Mono.error(new EntityExistsException("Автор с таким именем и фамилией уже есть в базе")) : Mono.empty())
                .switchIfEmpty(authorRepository.save(author))
                .then();
    }

    @Override
    public Mono<Author> getAuthorById(String authorId) {
        return authorRepository.findById(authorId)
                .switchIfEmpty(Mono.error(new NotFoundException("Не найден автор в базе для обновления")));
    }

    @Override
    public Flux<Author> getAuthorsById(Set<String> authorsId) {
        return authorRepository.findAllById(authorsId);
    }

    @Override
    public Mono<Void> updateAuthor(Author author) {
        return authorRepository.save(author)
                .then();
    }

    @Override
    public Mono<Void> deleteAuthor(String id) {
        return authorRepository.deleteById(id);
    }
}
