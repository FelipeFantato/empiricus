package com.example.gerenciador_usuarios;


import com.example.gerenciador_usuarios.model.Emails;
import com.example.gerenciador_usuarios.model.LoginRequest;
import com.example.gerenciador_usuarios.model.SessionManager;
import com.example.gerenciador_usuarios.model.Usuarios;
import com.example.gerenciador_usuarios.repo.EmailRepo;
import com.example.gerenciador_usuarios.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
public class PersonController {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    PersonRepo repo;

    @Autowired
    @Qualifier("emailRepo") // Use o nome do bean correto
    EmailRepo emailrepo;

    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Usuarios user = repo.findByNomeAndPassword(loginRequest.getNome(), loginRequest.getPassword());
        if (user != null && user.isEhAdmin()) {
            String token = sessionManager.createSession(user);
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nome ou senha incorretos, ou usuário não é administrador."+ user.isEhAdmin());
        }

    }
    @PostMapping("/api/usuario/deletar")
    public ResponseEntity<String> deletaUsuario(
            @RequestHeader("Authorization") String token,
            @RequestHeader("nome") String nome) {
        Usuarios usuarioDeletar = null;
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não esta logado.");
        }
        if(repo.findByNome(nome).size() != 0){
             usuarioDeletar= repo.findByNome(nome).get(0);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não existe.");
        }

        if( usuarioDeletar == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não existe.");
        }
        if(usuarioDeletar.getNome().equals(loggedInUser.getNome())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Não é possível excluir o seu próprio usuário.");
        }
        repo.delete(usuarioDeletar);


        return ResponseEntity.ok("Usuario: "+nome+" deletado com sucesso!");
    }

    @PostMapping("/api/usuario/alterar")
    public ResponseEntity<Usuarios> alteraUsuario(
            @RequestHeader("Authorization") String token,
            @RequestHeader("nome") String nome,
            @RequestBody Usuarios person) {

        Usuarios loggedInUser = sessionManager.getUser(token);
        Usuarios pessoa = null;
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if(repo.findByNome(nome).size() != 0){
            pessoa = repo.findByNome(nome).get(0);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (pessoa == null) {
            return ResponseEntity.notFound().build();
        }

        if(person.getNome() != null){
            pessoa.setNome(person.getNome());
        }
       if (person.getCpf() != null) {
           pessoa.setCpf(person.getCpf());
       }
        if (person.getPassword() != null) {
            pessoa.setPassword(person.getPassword());
        }
        if (person.getNome() != null || person.getCpf() != null || person.getPassword() != null) {
            pessoa.setDataAtualizacao(LocalDate.now());
            // Salvar
            repo.save(pessoa);
        }



        return ResponseEntity.ok(pessoa);
    }

    @GetMapping("/api/usuario")
    public ResponseEntity<Usuarios>  retornaUsuario(@RequestHeader("Authorization") String token,
                                                    @RequestHeader("nome") String nome){
        Usuarios pessoa = null;
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        if(repo.findByNome(nome).size() != 0){
            pessoa= repo.findByNome(nome).get(0);

        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }
        if (pessoa == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pessoa);

    }
    @GetMapping("/api/todos")
    public ResponseEntity<List<Usuarios>>  returnAll(@RequestHeader("Authorization") String token){
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Usuarios> persons = repo.findAll();
        return ResponseEntity.ok(persons);

    }

    @PostMapping("/api/addPerson")
    public ResponseEntity<String> addPerson(@RequestHeader("Authorization") String token,
                                            @RequestBody Usuarios person) {
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não está logado.");
        }

        if (!Objects.equals(person.camposCertos(), "OK")) {
            return ResponseEntity.badRequest().body(person.camposCertos());
        }
        if(person.getCpf().length() < 11){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF com menos de 11 digitos. Favor tentar novamente.");
        }
        repo.save(person);
        return ResponseEntity.ok("Usuário adicionado com sucesso.");
    }

    //APIs email ------------------------------------------------------------
    @PostMapping("/api/email/addEmail")
    public ResponseEntity<String> addEmail(@RequestHeader("Authorization") String token,
                                           @RequestBody Emails email,
                                           @RequestHeader("nome") String nome) {
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não está logado.");
        }

        if ( email.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Não foi possivel cadastrar pois está sem email.");
        }
        Usuarios pessoa = null;
        if(repo.findByNome(nome).size() != 0){
            pessoa = repo.findByNome(nome).get(0);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não existe.");
        }


        if (pessoa == null) {
            return ResponseEntity.notFound().build();
        }
        email.setIdUsuario(pessoa.getId());
        emailrepo.save(email);


        emailSenderService.sendEmail(email.getEmail(), pessoa.getCpf());
        return ResponseEntity.ok("Email adicionado com sucesso.");
    }
    @GetMapping("/api/email/emailsUsuario")
    public ResponseEntity<List<Emails>>  retornaEmailsUsuario(@RequestHeader("Authorization") String token,
                                                    @RequestHeader("nome") String nome){
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        Usuarios pessoa = null;
        if(repo.findByNome(nome).size() != 0){
            pessoa = repo.findByNome(nome).get(0);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        long idUsuario = pessoa.getId(); // Substitua pelo ID desejado
        List<Emails> listaFinal = emailrepo.findByIdUsuario(idUsuario);


        return ResponseEntity.ok(listaFinal);

    }
    @GetMapping("/api/email/todos")// validar
    public ResponseEntity<List<Emails>>  retornaTodosEmails(@RequestHeader("Authorization") String token){
        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Emails> emailsList = emailrepo.findAll();

        return ResponseEntity.ok(emailsList);

    }
    @PostMapping("/api/email/deletar")
    public ResponseEntity<String> deletaEmail(
            @RequestHeader("Authorization") String token,
            @RequestHeader("email") String email) {

        Usuarios loggedInUser = sessionManager.getUser(token);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario não esta logado.");
        }
        Emails emailDeletar= emailrepo.findByEmail(email);

        if( emailDeletar == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email não existe.");
        }
        Optional<Usuarios> usuarioOptional = repo.findById(emailDeletar.getIdUsuario());
        if (usuarioOptional.isPresent()) {
            Usuarios userAux = usuarioOptional.get();
            emailSenderService.sendEmail(email, userAux.getCpf());

        }


        emailrepo.delete(emailDeletar);
        return ResponseEntity.ok("Email: "+email+" deletado com sucesso!");
    }


}
