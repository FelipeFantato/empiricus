
package com.example.gerenciador_usuarios.repo;

import com.example.gerenciador_usuarios.model.Emails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepo extends JpaRepository<Emails, Long> {
    List<Emails> findByIdUsuario(long idUsuario);
    Emails findByEmail(String nome);

    @Query("select E.email from Emails E inner join Usuarios U on U.id =E.idUsuario where  U.eh_admin = true")
    String[] findByIdUsuarioEh_admin();
}