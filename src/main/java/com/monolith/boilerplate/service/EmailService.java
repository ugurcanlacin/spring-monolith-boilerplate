package com.monolith.boilerplate.service;

import com.monolith.boilerplate.dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    JmsTemplate jmsTemplate;

    private void sendEmail(EmailDTO email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());
        emailSender.send(message);
    }

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(EmailDTO email) {
        sendEmail(email);
    }

    public void sendEmailToQueue(EmailDTO email){
        jmsTemplate.convertAndSend("mailbox", email);
    }
}
