package com.springfreamwork.webflux.com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springfreamwork.webflux.domain.dto.GenreDTO;
import com.springfreamwork.webflux.domain.model.Genre;
import com.springfreamwork.webflux.domain.servicies.GenreService;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebFluxTest(GenreRestController.class)
public class GenreRestControllerTest {

    private final Genre genre = new Genre("genre_id", "novel");
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private GenreService genreService;

    @Test
    public void genreController_shouldCallDeleteGenre_andReturnSuccess() {
        when(genreService.deleteGenre(eq(genre.getId()))).thenReturn(Mono.empty());

        webClient.delete().uri("/deleteGenre?id={id}", genre.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(genreService, times(1)).deleteGenre(eq(genre.getId()));
    }

    @Test
    public void genreController_shouldReturnCollectionOfGenres() throws Exception{
        when(genreService.getAllGenres()).thenReturn(Flux.just(genre));

        String expected = mapper.writeValueAsString(Collections.singletonList(genre));

        webClient.get().uri("/getGenres")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(expected);

        verify(genreService, times(1)).getAllGenres();
    }

    @Test
    public void genreController_shouldCallEditGenre_andReturnSuccess() {
        when(genreService.updateGenre(eq(genre))).thenReturn(Mono.empty());

        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());

        webClient.post().uri("/editGenre")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(genreDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(genreService, times(1)).updateGenre(eq(genre));
    }

    @Test
    public void genreController_shouldCallCreateGenre_andReturnSuccess() {
        when(genreService.createGenre(eq(genre))).thenReturn(Mono.empty());

        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());

        webClient.post().uri("/createGenre")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(genreDTO))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("success");

        verify(genreService, times(1)).createGenre(eq(genre));
    }

}