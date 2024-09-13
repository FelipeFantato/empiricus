package com.example.empiricus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.empiricus.PersonController;
import com.example.empiricus.model.Emails;
import com.example.empiricus.model.LoginRequest;
import com.example.empiricus.model.SessionManager;
import com.example.empiricus.model.Usuarios;
import com.example.empiricus.repo.EmailRepo;
import com.example.empiricus.repo.PersonRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PersonControllerTest {

    @Mock
    private EmailSenderService emailSenderService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PersonRepo repo;

    @Autowired
    @InjectMocks
    private PersonController personController;

    @Autowired
    @Qualifier("emailRepo") // Use o nome do bean correto
    private EmailRepo emailrepo;

    public String token;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    @DisplayName("Retorna lista Usuarios se estiver Token Valido")
    void returnAll() {
        // Dados de teste
        Usuarios user1 = new Usuarios("1", "User One", true);

        String token  = this.sessionManager.createSession(user1);
        ResponseEntity<List<Usuarios>> responseEntity = this.personController.returnAll(token);

        // Verificação e asserção
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());


        //Limpeza das sessoes criadas para teste
        this.sessionManager.removeSession(token);

    }

    @Test
    @DisplayName("Nao Retorna lista Usuarios se estiver Token inválido")
    void returnAllInvalid() {

        ResponseEntity<List<Usuarios>> responseEntity = personController.returnAll("Invalid_token");

        // Verificação e asserção
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }



    @Test
    @DisplayName("Retorna chave JWT se Logado com sucesso")
    void loginCase1() {
        // Dados de teste
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setNome("admin");
        loginRequest.setPassword("admin");

        ResponseEntity<String> responseEntity =  this.personController.login(loginRequest);

        // Verificação e asserção
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isEmpty());


        //Limpeza das sessoes criadas para teste
        this.sessionManager.removeSession(token);

    }



    @Test
    @DisplayName("Nao retorna, pois esta sem o Usuario requisitado")
    void loginCase2() {
        // Dados de teste
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setNome("errado");
        loginRequest.setPassword("errado123");

        ResponseEntity<String> responseEntity =  this.personController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        this.sessionManager.removeSession(token);
    }


    @Test
    @DisplayName("Encontra usuario pelo Nome e Nao deleta pois é o que esta logado.")
    void deleteCase1() {
        this.sessionManager.removeSession(token);
        // Dados de teste

        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("1235");
        repo.delete(user1);


        Optional<Usuarios> userOptional = repo.findByCpf("1235");

        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));

        repo.save(user1);


        String token  = this.sessionManager.createSession(user1);
        ResponseEntity<String> responseEntity =  this.personController.deletaUsuario(token, "User One");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        this.sessionManager.removeSession(token);

    }

    @Test
    @DisplayName("Encontra usuario pelo Nome e  deleta")
    void deleteCase2() {
        // Dados de teste

        Usuarios user1 = new Usuarios("User One", "pwd123", true);

        user1.setCpf("1235");

        Optional<Usuarios> userOptional = repo.findByCpf("1235");

        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));

        Usuarios admin = new Usuarios("admin", "admin", true);

        String token  = this.sessionManager.createSession(admin);

        repo.save(user1);


        ResponseEntity<String> responseEntity =  this.personController.deletaUsuario(token, "User One");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        this.sessionManager.removeSession(token);

    }
    @Test
    @DisplayName("Nao Encontra usuario pelo Nome e nao deleta")
    void deleteCase3() {
        // Dados de teste

        Usuarios user1 = new Usuarios("User One", "pwd123", true);

        user1.setCpf("1235");

        Optional<Usuarios> userOptional = repo.findByCpf("1235");

        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));


        Usuarios admin = new Usuarios("admin", "admin", true);

        String token  = this.sessionManager.createSession(admin);

        repo.save(user1);


        ResponseEntity<String> responseEntity =  this.personController.deletaUsuario(token, "User 123");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().isEmpty());
        this.sessionManager.removeSession(token);

    }
    @Test
    @DisplayName("Altera Com Sucesso usuario Encontrado")
    void alteraCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("1235");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);
        repo.save(user1);

        Optional<Usuarios> userOptional2 = repo.findByCpf("123567");
        userOptional2.ifPresent(userDeletar -> repo.delete(userDeletar));

        Usuarios usuarioNovo = new Usuarios("User One123", "pwd123", true);
        usuarioNovo.setCpf("123567");

        ResponseEntity<Usuarios> responseEntity =  this.personController.alteraUsuario(token, "User One", usuarioNovo);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().getCpf().isEmpty());
        this.sessionManager.removeSession(token);
    }
    @Test
    @DisplayName("Altera Com Falha usuario nao Encontrado")
    void alteraCase2() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("1235");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);
        repo.save(user1);

        Usuarios usuarioNovo = new Usuarios("User One123", "pwd123", true);
        usuarioNovo.setCpf("1235");

        ResponseEntity<Usuarios> responseEntity =  this.personController.alteraUsuario(token, "User 123", usuarioNovo);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }


    @Test
    @DisplayName("Retorna usuario Valido")
    void retornaUsuarioCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("1235");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);
        repo.save(user1);


        ResponseEntity<Usuarios> responseEntity =  this.personController.retornaUsuario(token, "User One");


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertFalse(responseEntity.getBody().getCpf().isEmpty());
        this.sessionManager.removeSession(token);
    }

    @Test
    @DisplayName("Retorna erro usuario Inválido")
    void retornaUsuarioCase2() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("1235");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);
        repo.save(user1);


        ResponseEntity<Usuarios> responseEntity =  this.personController.retornaUsuario(token, "User One1234");


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }


    @Test
    @DisplayName("Adiciona Pessoa com Cpf valido(11 digitos)")
    void addPersonCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("12345678911");
        Optional<Usuarios> userOptional = repo.findByCpf("12345678911");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);


        ResponseEntity<String> responseEntity =  this.personController.addPerson(token, user1);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }

    @Test
    @DisplayName("Erro Pessoa com Cpf invalido( menos de 11 digitos)")
    void addPersonCase2() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);


        ResponseEntity<String> responseEntity =  this.personController.addPerson(token, user1);


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }

    @Test
    @DisplayName("Adiciona Email Pessoa valida")
    void addEmailCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("123");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        repo.save(user1);
        String token  = this.sessionManager.createSession(admin);

        Emails novoEmail = new Emails("teste@teste.com", user1.getId());
        ResponseEntity<String> responseEntity =  this.personController.addEmail(token,novoEmail,"User One"  );


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }
    @Test
    @DisplayName("Erro adicionar Email Pessoa Invalida")
    void addEmailCase2() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("123");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);

        Emails novoEmail = new Emails("teste@teste.com", user1.getId());
        ResponseEntity<String> responseEntity =  this.personController.addEmail(token,novoEmail,"User One234"  );


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }
    @Test
    @DisplayName("Retorna Lista valida")
    void retornaEmailsUsuarioCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("123");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);
        repo.save(user1);
        Emails novoEmail = new Emails("teste@teste.com", user1.getId());
        ResponseEntity<List<Emails>> responseEntity =  this.personController.retornaEmailsUsuario(token,"User One"  );


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }
    @Test
    @DisplayName("Erro  Email Pessoa Invalida")
    void retornaEmailsUsuarioCase2() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);

        Emails novoEmail = new Emails("teste@teste.com", user1.getId());
        ResponseEntity<List<Emails>> responseEntity =  this.personController.retornaEmailsUsuario(token,"User One234"  );


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }
    @Test
    @DisplayName("Retorna todos Emails")
    void retornaTodosEmailsCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);

        Emails novoEmail = new Emails("teste@teste.com", user1.getId());
        ResponseEntity<List<Emails>> responseEntity =  this.personController.retornaTodosEmails(token );


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }

    @Test
    @DisplayName("Deleta email existente")
    void deletaEmailCase1() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);


        Emails novoEmail = new Emails("teste@teste.com", user1.getId());

        Optional<Emails> userOptionalEmail = Optional.ofNullable(emailrepo.findByEmail("teste@teste.com"));
        userOptionalEmail.ifPresent(novoEmailDeletar -> emailrepo.delete(novoEmailDeletar));


        this.personController.addEmail(token,novoEmail,"User One"  );

        ResponseEntity<String> responseEntity =  this.personController.deletaEmail(token,"teste@teste.com"  );


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }
    @Test
    @DisplayName("Erro  Deleta email inexistente")
    void deletaEmailCase2() {
        // Dados de teste
        Usuarios user1 = new Usuarios("User One", "pwd123", true);
        user1.setCpf("123");
        Optional<Usuarios> userOptional = repo.findByCpf("1235");
        userOptional.ifPresent(userDeletar -> repo.delete(userDeletar));
        Usuarios admin = new Usuarios("admin", "admin", true);
        String token  = this.sessionManager.createSession(admin);

        Emails novoEmail = new Emails("teste@teste.com", user1.getId());
        ResponseEntity<String> responseEntity =  this.personController.deletaEmail(token,"teste123@teste.com"  );


        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        this.sessionManager.removeSession(token);
    }

}
