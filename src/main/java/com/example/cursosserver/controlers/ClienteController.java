package com.example.cursosserver.controlers;

import com.example.cursosserver.dtos.ClienteDto;
import com.example.cursosserver.exceptions.ObjetoInexistenteException;
import com.example.cursosserver.models.Cliente;
import com.example.cursosserver.services.ClienteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Clientes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Log4j2
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping("/New")
    public ResponseEntity<String> save(@RequestBody @Valid ClienteDto dto){
        try {
            this.service.salvarCliente(dto.tranform());
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/Todos")
    public ResponseEntity<List<Cliente>> listar(){

        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.listarTodos());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/Edit")
    public ResponseEntity<String> atualizar(@RequestBody @Valid ClienteDto dto
            , @RequestParam(name = "id") @NotBlank Long id){

        try{
            service.atualizarCliente(dto.tranform(), id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(ObjetoInexistenteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/Delete")
    public ResponseEntity<String> remover(@RequestParam(name = "id") @NotBlank Long id){

        try{
            service.apagarCliente(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch(ObjetoInexistenteException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
