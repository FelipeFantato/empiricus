package com.example.empiricus;

import com.example.empiricus.model.SessionManager;
import com.example.empiricus.model.Usuarios;
import com.example.empiricus.repo.EmailRepo;
import com.example.empiricus.repo.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@DataJpaTest
@ActiveProfiles("test")
class PersonControllerTest {

    @Mock
    private EmailSenderService emailSenderService;

    @Autowired
    PersonRepo PersonRepo;
    @Mock
    @Qualifier("emailRepo") // Use o nome do bean correto
    EmailRepo emailrepo;

    private SessionManager sessionManager = new SessionManager();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    @DisplayName("Teste de login Sucesso É Admin")
    void loginCase1() {
        Usuarios usuarioLogando = new Usuarios();
        usuarioLogando.setNome("Felipe");
        usuarioLogando.setPassword("123456");
        usuarioLogando.setEh_admin(true);
        PersonRepo.save(usuarioLogando);

        Usuarios usuarioNoBanco = PersonRepo.findByNomeAndPassword("Felipe", "123456");

        assertThat(usuarioNoBanco.isEh_admin()).isTrue();
    }

    @Test
    @DisplayName("Teste de login Falha Nao é Admin")
    void loginCase2() {
        Usuarios usuarioLogando = new Usuarios();
        usuarioLogando.setNome("Felipe");
        usuarioLogando.setPassword("123456");
        usuarioLogando.setEh_admin(false);
        PersonRepo.save(usuarioLogando);
        Usuarios usuarioNoBanco = PersonRepo.findByNomeAndPassword("Felipe", "123456");

        assertThat(usuarioNoBanco.isEh_admin()).isFalse();

    }
    @Test
    @DisplayName("Deleta Usuario existente")
    void deletaUsuarioCase1() {
        Usuarios usuarioLogando = new Usuarios();
        usuarioLogando.setNome("Felipe");
        usuarioLogando.setPassword("123456");
        usuarioLogando.setEh_admin(true);
        PersonRepo.save(usuarioLogando);

        PersonRepo.delete(usuarioLogando);
        assertThat(PersonRepo.findByNomeAndPassword("Felipe", "123456")).isNull();
    }

    @Test
    @DisplayName("Deleta Usuario inexistente")
    void deletaUsuarioCase2() {
        Usuarios usuarioLogando = new Usuarios();
        usuarioLogando.setNome("Felipe");
        usuarioLogando.setPassword("123456");
        usuarioLogando.setEh_admin(true);


        PersonRepo.delete(usuarioLogando);
        assertThat(PersonRepo.findByNomeAndPassword("Felipe", "123456")).isNull();
    }

    @Test
    @DisplayName("Altera Usuario Válido")
    void alteraUsuario() {


    }

    @Test
    void retornaUsuario() {
    }

    @Test
    void addPerson() {
    }

    @Test
    void addEmail() {
    }

    @Test
    void retornaEmailsUsuario() {
    }

    @Test
    void retornaTodosEmails() {
    }

    @Test
    void deletaEmail() {
    }
}