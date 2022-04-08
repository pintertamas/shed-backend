package hu.bme.aut.shed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplyshed@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendRegistrationMessage(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreplyshed@gmail.com");
        message.setTo(to);
        message.setSubject("Registration");
        message.setText("Thanks for register for our game.GLHF");
        emailSender.send(message);
    }
}
