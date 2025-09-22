package com.example.prova_intermediaria.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "livros")
public class Livro {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    @Size(max = 200)
    private String titulo;


    @NotBlank
    @Size(max = 150)
    private String autor;


    @NotBlank
    @Size(max = 50)
    private String genero; // exemplos: "Ficção", "Técnico", "História"


    @NotNull
    private Integer anoPublicacao;


    @Column(nullable = false)
    private LocalDateTime dataCadastro;


    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }



    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }


    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }


    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }


    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }


    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
