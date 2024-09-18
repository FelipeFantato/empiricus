package com.example.gerenciador_usuarios.repo;

import com.example.gerenciador_usuarios.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface PersonRepo extends JpaRepository<Usuarios, Long> {
    Usuarios findByNomeAndPassword(String nome, String password);

    List<Usuarios> findByNome(String nome);


    Optional<Usuarios> findByCpf(String cpf);
}
