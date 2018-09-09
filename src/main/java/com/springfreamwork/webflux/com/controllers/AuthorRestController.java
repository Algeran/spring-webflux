package com.springfreamwork.webflux.com.controllers;

import com.springfreamwork.webflux.domain.dto.AuthorDTO;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Country;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AuthorRestController {

    private final AuthorService authorService;

    @Autowired
    public AuthorRestController(
            AuthorService authorService
    ) {
        this.authorService = authorService;
    }

    @GetMapping("/getAuthors")
    public Flux<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @DeleteMapping("/deleteAuthor")
    public Mono<String> deleteGenre(@RequestParam("id") String id) {
        return authorService.deleteAuthor(id)
                .then(Mono.just("success"));
    }

    @PostMapping("/editAuthor")
    public Mono<String> editAuthor(
            @RequestBody AuthorDTO authorDTO
            ) {

        return authorService.getAuthorById(authorDTO.getId())
                .map(author -> {
                    author.setName(authorDTO.getName());
                    author.setSurname(authorDTO.getSurname());
                    return author;
                })
                .flatMap(authorService::updateAuthor)
                .then(Mono.just("success"));
    }


    @GetMapping("/getCountries")
    public Flux<Country> getAllCountries() {
        return Flux.fromArray(Country.values());
    }

    @PostMapping("/createAuthor")
    public Mono<String> createAuthor(@RequestBody AuthorDTO authorDTO) {
        return Mono.just(authorDTO)
                .map(AuthorDTO::toAuthor)
                .flatMap(authorService::createAuthor)
                .then(Mono.just("success"));
    }
}
