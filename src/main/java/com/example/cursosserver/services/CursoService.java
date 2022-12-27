package com.example.cursosserver.services;

import com.example.cursosserver.exceptions.ObjetoInexistenteException;
import com.example.cursosserver.models.Curso;
import com.example.cursosserver.repositories.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;
    @Autowired
    private SendMailService sendMailService;

    private final String adressMail = "lucaswalison136@gmail.com";

    @Transactional
    public List<Curso> listarTodos() throws RuntimeException{
        return this.repository.findAll();
    }

    @Transactional
    public Curso getByNome(String nome) throws ObjetoInexistenteException {

        boolean exist = repository.existsByNome(nome);

        if(!exist){
            throw new ObjetoInexistenteException("Curso Inexistente!");
        }
        return repository.findByNome(nome);
    }

    @Transactional
    public void salvarCurso(Curso curso) throws RuntimeException{

        boolean exist = repository.existsByNome(curso.getNome());
        if(exist){throw new RuntimeException();}

        System.out.println("Curso: [ "+ curso.getNome() + " ] Salvo!");
        this.repository.save(curso);
        sendMailService.createMailAndSendWithAttachments(adressMail, curso);
    }

    @Transactional
    public void atualizarCurso(Curso curso, String nome) throws ObjetoInexistenteException{

        boolean exist = repository.existsByNome(nome);
        boolean existCursoName = repository.existsByNome(curso.getNome());

        if(!exist){throw new ObjetoInexistenteException("Curso Inexistente!");}

        //Verifica se á distinção dos dados antigos em relação aos dados novos.

        boolean verificar1 = curso.getNome().equals(nome)
                && repository.findByNome(nome).getCategoria().equals(curso.getCategoria());

        //Verifica a existência do curso
        boolean verificar2 = existCursoName && !curso.getNome().equals(nome);

        if(verificar2 || verificar1){
            throw new RuntimeException();
        }

        Curso cursoAntigo = repository.findByNome(nome);
        curso.setId(cursoAntigo.getId());
        repository.save(curso);
    }
    @Transactional
    public void apagarCurso(String nome) throws ObjetoInexistenteException{

        if(!repository.existsByNome(nome)){
            throw new ObjetoInexistenteException("Curso Inexistente!");
        }

        repository.deleteByNome(nome);
    }
}
