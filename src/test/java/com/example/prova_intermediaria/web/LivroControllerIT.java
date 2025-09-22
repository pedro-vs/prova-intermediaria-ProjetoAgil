package com.example.prova_intermediaria.web;

import com.example.prova_intermediaria.model.Livro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LivroControllerIT {


    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;


    @Test
    @DisplayName("POST /api/livros cria e retorna 201")
    void criarLivro() throws Exception {
        var body = new java.util.HashMap<String,Object>();
        body.put("titulo", "Clean Code");
        body.put("autor", "Robert C. Martin");
        body.put("genero", "Técnico");
        body.put("anoPublicacao", 2008);


        mvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.dataCadastro", notNullValue()))
                .andExpect(jsonPath("$.titulo", is("Clean Code")));
    }


    @Test
    @DisplayName("GET /api/livros lista incluindo o cadastrado")
    void listarLivros() throws Exception {
// garante 1 registro
        criarLivro();
        mvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }


    @Test
    @DisplayName("DELETE /api/livros/{id} remove e retorna 204; 404 quando não existe")
    void deletarLivroE404() throws Exception {
        // cria
        var body = new java.util.HashMap<String, Object>();
        body.put("titulo", "DDD");
        body.put("autor", "Eric Evans");
        body.put("genero", "Técnico");
        body.put("anoPublicacao", 2003);

        var result = mvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andReturn();

        var criado = om.readValue(result.getResponse().getContentAsString(), Livro.class);

        // deleta OK
        mvc.perform(delete("/api/livros/" + criado.getId()))
                .andExpect(status().isNoContent());

        // tenta deletar de novo -> 404
        mvc.perform(delete("/api/livros/" + criado.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Livro não encontrado: id=" + criado.getId()));
    }



    @Test
    @DisplayName("POST validação 400 quando campos obrigatórios faltam")
    void validacaoCampos() throws Exception {
        var body = new java.util.HashMap<String,Object>();
        body.put("titulo", ""); // inválido
        body.put("autor", "Autor X");
        body.put("genero", "Técnico");
        body.put("anoPublicacao", 2020);


        mvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/livros retorna array (pode estar vazio)")
    void listarVazioOuNao() throws Exception {
        mvc.perform(get("/api/livros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("POST /api/livros 400 quando título em branco")
    void postComTituloVazioDa400() throws Exception {
        var body = new java.util.HashMap<String,Object>();
        body.put("titulo", "");              // inválido
        body.put("autor", "Alguém");
        body.put("genero", "Técnico");
        body.put("anoPublicacao", 2020);

        mvc.perform(post("/api/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

}
