package com.example.prova_intermediaria.web;


import com.example.prova_intermediaria.model.Livro;
import com.example.prova_intermediaria.service.LivroService;
import com.example.prova_intermediaria.web.dto.ErroResponse;
import com.example.prova_intermediaria.web.dto.LivroRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/livros")
public class LivroController {


    private final LivroService service;


    public LivroController(LivroService service) {
        this.service = service;
    }


    // POST /api/livros -> cadastra
    @PostMapping
    public ResponseEntity<Livro> criar(@Valid @RequestBody LivroRequest request) {
        Livro livro = new Livro();
        livro.setTitulo(request.titulo);
        livro.setAutor(request.autor);
        livro.setGenero(request.genero);
        livro.setAnoPublicacao(request.anoPublicacao);
        Livro salvo = service.criar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }


    // GET /api/livros -> lista todos
    @GetMapping
    public List<Livro> listarTodos() {
        return service.listarTodos();
    }


    // DELETE /api/livros/{id} -> excluir
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }


    // Tratamento simples de erros de validação
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErroResponse(msg));
    }


    // Tratamento para not found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().startsWith("Livro não encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponse(ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErroResponse("Erro interno"));
    }
}