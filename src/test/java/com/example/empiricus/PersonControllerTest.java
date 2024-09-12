package com.example.empiricus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.example.empiricus.PersonController;
import com.example.empiricus.model.SessionManager;
import com.example.empiricus.model.Usuarios;
import com.example.empiricus.repo.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class PersonControllerTest {

    @Mock
    private EmailSenderService emailSenderService;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PersonRepo repo;

    @Autowired
    private PersonController personController;

    public String token;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void returnAll_ShouldReturnUsers_WhenValidToken() {
        // Dados de teste
        Usuarios user1 = new Usuarios("1", "User One", true);
        Usuarios user2 = new Usuarios("2", "User Two", true);
        List<Usuarios> users = Arrays.asList(user1, user2);

        token = this.sessionManager.createSession(user1);
        // Configuração dos mocks
        String token = this.sessionManager.createSession(user1);

        //when(sessionManager.getUser("Valid_token")).thenReturn(user1);
        //when(this.repo.findAll()).thenReturn(users);


        ResponseEntity<List<Usuarios>> responseEntity = this.personController.returnAll(token);

        // Verificação e asserção
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        assertTrue(responseEntity.getBody().contains(user1));
        assertTrue(responseEntity.getBody().contains(user2));
    }

    @Test
    void returnAll_ShouldReturnUnauthorized_WhenInvalidToken() {
        // Configuração do mock
        when(sessionManager.getUser("Invalid_token")).thenReturn(null);

        // Chamada ao método do controller
        ResponseEntity<List<Usuarios>> responseEntity = personController.returnAll("Invalid_token");

        // Verificação e asserção
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
