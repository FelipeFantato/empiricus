package com.example.gerenciador_usuarios.model;
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
    public Usuarios(){}
    public Usuarios(String nome, String password, boolean eh_admin) {
        this.nome = nome;
        this.password = password;
        this.eh_admin = eh_admin;
        this.data_criacao = Date.valueOf(LocalDate.now());
        this.data_atualizacao = Date.valueOf(LocalDate.now());
    }
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {

        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setEh_admin(boolean b) {
        this.eh_admin = b;
    }

    public boolean isEh_admin() {
        return this.eh_admin;
    }
}
