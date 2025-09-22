package com.example.prova_intermediaria.web;

import com.example.prova_intermediaria.model.Livro;
import com.example.prova_intermediaria.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivroController.class)
class LivroControllerWebMvcTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockBean LivroService service;

    @Test
    @DisplayName("POST /api/livros -> 201 com corpo")
    void postCria() throws Exception {
        Livro salvo = new Livro();
        salvo.setId(10L);
        salvo.setTitulo("Clean Code");
        salvo.setAutor("Robert C. Martin");
        salvo.setGenero("Técnico");
        salvo.setAnoPublicacao(2008);
        salvo.setDataCadastro(LocalDateTime.now());

        // quando o controller chamar service.criar(...)
        when(service.criar(argThat(l -> "Clean Code".equals(l.getTitulo())))).thenReturn(salvo);

        var body = """
        {
          "titulo":"Clean Code",
          "autor":"Robert C. Martin",
          "genero":"Técnico",
          "anoPublicacao":2008
        }
        """;

        mvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.titulo").value("Clean Code"));
    }

    @Test
    @DisplayName("GET /api/livros -> 200 lista")
    void getLista() throws Exception {
        Livro l = new Livro();
        l.setId(1L); l.setTitulo("TDD"); l.setAutor("Kent Beck");
        l.setGenero("Técnico"); l.setAnoPublicacao(2002);
        l.setDataCadastro(LocalDateTime.now());

        when(service.listarTodos()).thenReturn(List.of(l));

        mvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("TDD"));
    }

    @Test
    @DisplayName("DELETE /api/livros/{id} -> 204 e depois 404 no handler")
    void deleteFluxos() throws Exception {
        // primeira chamada (nada a configurar, retorna 204)
        mvc.perform(delete("/api/livros/5"))
                .andExpect(status().isNoContent());

        // segunda chamada simula exceção do service
        doThrow(new RuntimeException("Livro não encontrado: id=5"))
                .when(service).excluir(5L);

        mvc.perform(delete("/api/livros/5"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Livro não encontrado: id=5"));
    }
}
