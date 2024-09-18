package com.example.empiricus.repo;

import com.example.gerenciador_usuarios.model.Usuarios;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class PersonRepoTest {
    @Autowired
    com.example.gerenciador_usuarios.repo.PersonRepo PersonRepo;

    @Autowired
    EntityManager entityManager;

    @Test
    void findByNomeAndPassword() {
    }

    @Test
    @DisplayName("Precisa retornar o Usuario com Sucesso")
    void findByNomeCase1() {
        Usuarios usuario = new Usuarios("Sucesso","Sucesso",true);

        usuario.setCpf("12345678922");
        PersonRepo.save(usuario);
        String nome = usuario.getNome();
        Usuarios result = this.PersonRepo.findByNome(nome).get(0);

        assertThat(result != null).isTrue();
    }
    @Test
    @DisplayName("Precisa retornar o Usuario com Falha caso nao exista")
    void findByNomeCase2() {
        String nome = "Josefino";
        List<Usuarios> listResult = this.PersonRepo.findByNome(nome);

        assertThat(listResult.size() == 0).isTrue();
    }

    private Usuarios createUsuario(Usuarios usuario) {
        Usuarios newUser = new Usuarios("Sucesso","Sucesso",true);
        this.entityManager.persist(usuario);
        return newUser;
    }


}