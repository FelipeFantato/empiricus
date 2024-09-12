package com.example.empiricus.repo;

import com.example.empiricus.model.Emails;
import com.example.empiricus.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface PersonRepo extends JpaRepository<Usuarios, Long> {
    Usuarios findByNomeAndPassword(String nome, String password);

    List<Usuarios> findByNome(String nome);



}
