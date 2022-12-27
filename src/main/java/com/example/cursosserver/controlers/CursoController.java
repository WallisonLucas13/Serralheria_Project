package com.example.cursosserver.controlers;

import com.example.cursosserver.dtos.CursoDto;
import com.example.cursosserver.exceptions.ObjetoInexistenteException;
import com.example.cursosserver.models.Curso;
import com.example.cursosserver.services.CursoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
@Log4j2
public class CursoController {

    @Autowired
    private CursoService service;

    @PostMapping("/Cursos/New")
    public ResponseEntity<String> save(@RequestBody @Valid CursoDto dto){
        try {
            this.service.salvarCurso(dto.tranform());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/Cursos/Todos")
    public ResponseEntity<List<Curso>> listar(){

        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/Cursos/Details")
    public ResponseEntity<Curso> getCurso(@RequestParam(name = "nome") @Valid String nome){

        try{
            Curso curso = service.getByNome(nome);
            System.out.println("Curso: [ "+ nome +" ] Requisitado!");
            return ResponseEntity.status(HttpStatus.OK).body(curso);

        }catch(ObjetoInexistenteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/Cursos/Edit")
    public ResponseEntity<String> atualizar(@RequestBody @Valid CursoDto dto
            , @RequestParam(name = "nome") @NotBlank String nome){

        try{
            service.atualizarCurso(dto.tranform(), nome);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(ObjetoInexistenteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/Cursos/Delete")
    public ResponseEntity<String> remover(@RequestParam(name = "nome") @NotBlank String nome){

        System.out.println("Removendo: [ " + nome +" ]");
        try{
            service.apagarCurso(nome);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(ObjetoInexistenteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
