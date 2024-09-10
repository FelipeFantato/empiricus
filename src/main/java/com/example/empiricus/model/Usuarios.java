package com.example.empiricus.model;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Entity
@Table(name="usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String nome;
    private String cpf;
    private String password;
    private Date data_criacao;
    private Date data_atualizacao;
    private boolean eh_admin;

    public String camposCertos() {
        String resultado = "OK";
        if (cpf.length() != 11){
            return "CPF não possui 11 caracteres.";
        }
        if (!cpf.matches("\\d+")) {
            return "O campo CPF deve conter apenas números.";
        }

        return resultado;
    }

    public boolean isEhAdmin() {
        return  eh_admin;
    }

    public void setDataAtualizacao(LocalDate now) {
        this.data_atualizacao = Date.valueOf(now);
    }
}
