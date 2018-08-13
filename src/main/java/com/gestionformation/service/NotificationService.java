package com.gestionformation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(String email, String msg) throws MailException { // user une classe a un nom prenom adressemail

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(email);
        mail.setFrom("PCD.Ensi.Soutenance@gmail.com");
        mail.setSubject("Reservation du formation");
        mail.setText(msg);


        javaMailSender.send(mail);
    }

}

