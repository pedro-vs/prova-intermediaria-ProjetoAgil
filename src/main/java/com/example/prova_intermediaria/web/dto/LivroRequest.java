package com.example.prova_intermediaria.web.dto;


import jakarta.validation.constraints.*;


public class LivroRequest {
    @NotBlank
    @Size(max = 200)
    public String titulo;


    @NotBlank
    @Size(max = 150)
    public String autor;


    @NotBlank
    @Size(max = 50)
    public String genero;


    @NotNull
    public Integer anoPublicacao;
}