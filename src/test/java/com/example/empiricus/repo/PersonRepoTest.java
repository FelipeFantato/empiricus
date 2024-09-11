package com.example.empiricus.repo;

import com.example.empiricus.model.Usuarios;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PersonRepoTest {
    @Autowired
    PersonRepo PersonRepo;

    @Autowired
    EntityManager entityManager;

    @Test
    void findByNomeAndPassword() {
    }

    @Test
    @DisplayName("Precisa retornar o Usuario com Sucesso")
    void findByNomeCase1() {
        Usuarios usuario = new Usuarios();
        usuario.setNome("Sucesso");
        usuario.setPassword("Sucesso");
        usuario.setCpf("12345678922");
        PersonRepo.save(usuario);
        String nome = usuario.getNome();
        Usuarios result = this.PersonRepo.findByNome(nome);

        assertThat(result != null).isTrue();
    }
    @Test
    @DisplayName("Precisa retornar o Usuario com Falha caso nao exista")
    void findByNomeCase2() {
        String nome = "Josefino";
        Usuarios result = this.PersonRepo.findByNome(nome);

        assertThat(result != null).isFalse();
    }

    private Usuarios createUsuario(Usuarios usuario) {
        Usuarios newUser = new Usuarios();
        this.entityManager.persist(usuario);
        return newUser;
    }
}