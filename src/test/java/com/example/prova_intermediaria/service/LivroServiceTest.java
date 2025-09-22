package com.example.prova_intermediaria.service;


import com.example.prova_intermediaria.model.Livro;
import com.example.prova_intermediaria.repository.LivroRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Import(LivroService.class)
@ActiveProfiles("test")
class LivroServiceTest {


    @Autowired LivroRepository repo;
    @Autowired LivroService service;


    @Test
    void criarListarExcluir() {
        Livro l = new Livro();
        l.setTitulo("Test Title");
        l.setAutor("Tester");
        l.setGenero("Ficção");
        l.setAnoPublicacao(2024);


        var salvo = service.criar(l);
        assertNotNull(salvo.getId());
        assertNotNull(salvo.getDataCadastro());


        var todos = service.listarTodos();
        assertFalse(todos.isEmpty());


        service.excluir(salvo.getId());
        assertTrue(repo.findById(salvo.getId()).isEmpty());
    }


    @Test
    void excluirNaoExistenteLanca() {
        var ex = assertThrows(RuntimeException.class, () -> service.excluir(9999L));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }
}