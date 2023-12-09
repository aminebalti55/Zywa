package com.example.zywaa.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service

public class EmailService {



    @Autowired
     JavaMailSender javaMailSender;

    public void sendEmailWithAttachment(String to, String subject, String body, InputStream attachment, String filename) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            helper.addAttachment(filename, new InputStreamSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return new ByteArrayInputStream(((ByteArrayInputStream) attachment).readAllBytes());
                }
            });

            javaMailSender.send(message);
        } catch (MessagingException e) {

        }
    }


}
