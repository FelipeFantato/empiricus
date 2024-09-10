package com.example.empiricus.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "emails")
public class Emails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "idUsuario")
    private long idUsuario;

    @Column(name = "email")
    private String email;
    private Date dataCriacao;
    private Date dataAtualizacao;
}
