package com.example.empiricus.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

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

    public Emails(){}

    public Emails(String email, Long idUsuario) {
        this.email = email;
        this.idUsuario = idUsuario;
        this.dataCriacao = Date.valueOf(LocalDate.now());
    }
    public String getEmail() {
        return email;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setEmail(String email){
        this.email = email;
    }

}
