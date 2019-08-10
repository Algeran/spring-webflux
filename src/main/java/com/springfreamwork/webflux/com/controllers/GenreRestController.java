package com.springfreamwork.webflux.com.controllers;

import com.springfreamwork.webflux.domain.dto.GenreDTO;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.GenreService;
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
public class GenreRestController {

    private final GenreService genreService;

    @Autowired
    public GenreRestController(
            GenreService genreService
    ) {
        this.genreService = genreService;
    }

    @DeleteMapping("/deleteGenre")
    public Mono<String> deleteGenre(@RequestParam("id") String id) {
        return genreService.deleteGenre(id)
                .then(Mono.just("success"));
    }

    @GetMapping("/getGenres")
    public Flux<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @PostMapping("/editGenre")
    public Mono<String> editGenre(
            @RequestBody GenreDTO genreDTO) {
        return Mono.just(genreDTO)
                .map(GenreDTO::toGenre)
                .flatMap(genreService::updateGenre)
                .then(Mono.just("success"));
    }

    @PostMapping("/createGenre")
    public Mono<String> createGenre(
            @RequestBody GenreDTO genreDTO) {
        return Mono.just(genreDTO)
                .map(GenreDTO::toGenre)
                .flatMap(genreService::createGenre)
                .then(Mono.just("success"));
    }

}
