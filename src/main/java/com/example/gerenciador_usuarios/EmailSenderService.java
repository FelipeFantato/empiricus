package com.example.gerenciador_usuarios;

import com.example.gerenciador_usuarios.repo.EmailRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    @Autowired
    @Qualifier("emailRepo") // Use o nome do bean correto
    EmailRepo emailrepo;

    @Autowired
    private JavaMailSender mailSender;
    @Async("asyncTaskExecutor")
    public void sendEmail(String email, String cpf){
        String subject = "O email "+email+" foi criado/alterado para o usuário de CPF "+cpf;
        String body = " Teste123";
        SimpleMailMessage message = new SimpleMailMessage();
        String[] listaEmails = emailrepo.findByIdUsuarioEh_admin();
        message.setTo(listaEmails);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);

        System.out.println("Email Enviado com sucesso!");
    }


}

