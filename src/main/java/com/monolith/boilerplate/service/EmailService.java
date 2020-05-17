package com.monolith.boilerplate.service;

import com.monolith.boilerplate.dto.EmailDTO;
import com.monolith.boilerplate.model.CommunicationLogEntity;
import com.monolith.boilerplate.repository.CommunicationLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    CommunicationLogRepository communicationLogRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    private void sendEmail(EmailDTO email) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            emailSender.send(message);
            log.info("Email is sent. EmailDTO: {}", email);
            CommunicationLogEntity communicationLogEntity = CommunicationLogEntity.builder().communicationType("Email").messageType("Verification").receiverId(email.getReceiverId()).message(email.getText()).build();
            communicationLogRepository.save(communicationLogEntity);
        } catch (Exception ex){
            log.error("Email sending is failed. EmailDTO: {}", email);
        }

    }

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(EmailDTO email) {
        sendEmail(email);
    }

    public void sendEmailToQueue(EmailDTO email){
        jmsTemplate.convertAndSend("mailbox", email);
    }
}
