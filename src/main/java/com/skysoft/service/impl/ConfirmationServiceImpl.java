package com.skysoft.service.impl;

import com.skysoft.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.message}")
    private String text;

    @Async
    @Override
    public void sendConfirmation(String email, UUID id, UUID confirmationCode) {
        log.info("[x] Ready send confirmation : {}, code: {}", email, confirmationCode);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(text + "\n" + "http://localhost:8088/api/access/confirm?id=" + id +
                "&confirmationCode=" + confirmationCode);
        mailSender.send(message);
        log.info("[x] Confirmation send successfully.");
    }
}
