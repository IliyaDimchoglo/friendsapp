package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.message}")
    private String text;

    @Override
    public void sendConfirmation(String email, String confirmationCode) {
        log.info("[x] Ready to send confirmation to: {}, code: {}", email, confirmationCode);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(text + "\n" + "http://localhost:8888/api/access/confirm?confirmationCode=" + confirmationCode);
        mailSender.send(message);
        log.info("[x] Confirmation send successfully");
    }
}
