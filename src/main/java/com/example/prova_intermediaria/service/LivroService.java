package com.example.prova_intermediaria.service;


import com.example.prova_intermediaria.model.Livro;
import com.example.prova_intermediaria.repository.LivroRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class LivroService {
    private final LivroRepository repository;


    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }


    public Livro criar(Livro livro) {
        return repository.save(livro);
    }


    public List<Livro> listarTodos() {
        return repository.findAll();
    }


    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Livro não encontrado: id=" + id);
        }
        repository.deleteById(id);
    }
}

