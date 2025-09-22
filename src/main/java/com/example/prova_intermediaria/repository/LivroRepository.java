package com.example.prova_intermediaria.repository;


import com.example.prova_intermediaria.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LivroRepository extends JpaRepository<Livro, Long> {
}
