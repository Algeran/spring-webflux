package com.springfreamwork.webflux.com.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springfreamwork.webflux.domain.dto.AuthorDTO;
import com.springfreamwork.webflux.domain.model.Author;
import com.springfreamwork.webflux.domain.model.Country;
import com.springfreamwork.webflux.domain.servicies.AuthorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static com.springfreamwork.webflux.domain.model.Country.RUSSIA;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(AuthorRestController.class)
public class AuthorRestControllerTest {

    private final Author author = new Author("author_id", "Leo", "Tolstoy", RUSSIA);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private AuthorService authorService;

    @Test
    public void authorRestController_shouldReturnAllAuthors() throws JsonProcessingException {
        when(authorService.getAllAuthors()).thenReturn(Flux.just(author));

        String expected = mapper.writeValueAsString(Collections.singletonList(author));

        webClient.get().uri("/getAuthors")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expected);

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    public void authorRestController_shouldCallDeleteAuthor_andReturnSuccess() {
        when(authorService.deleteAuthor(eq(author.getId()))).thenReturn(Mono.empty());

        webClient.delete().uri("/deleteAuthor?id={id}", author.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(authorService, times(1)).deleteAuthor(eq(author.getId()));
    }

    @Test
    public void authorRestController_shouldCallEditAuthor_andReturnSuccess() {
        when(authorService.getAuthorById(eq(author.getId()))).thenReturn(Mono.just(author));
        when(authorService.updateAuthor(eq(author))).thenReturn(Mono.empty());

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setSurname(author.getSurname());
        authorDTO.setCountry(author.getCountry());

        webClient.post().uri("/editAuthor")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(authorDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(authorService, times(1)).getAuthorById(eq(author.getId()));
        verify(authorService, times(1)).updateAuthor(eq(author));
    }

    @Test
    public void authorRestController_shouldReturnArrayOfCountries() throws JsonProcessingException {
        String expected = mapper.writeValueAsString(Country.values());

        webClient.get().uri("/getCountries")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expected);
    }

    @Test
    public void authorRestController_shouldCallCreateAuthor_andReturnSuccess() {
        when(authorService.createAuthor(any())).thenReturn(Mono.empty());

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setSurname(author.getSurname());
        authorDTO.setCountry(author.getCountry());

        webClient.post().uri("/createAuthor")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(authorDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");
    }

}