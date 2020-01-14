package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationServiceImpl implements ConfirmationService {

    private final JavaMailSender mailSender;

    @Override
    public void sendConfirmation(String email, String confirmationCode) {
        log.info("[x] Ready to send confirmation to: {}, code: {}", email, confirmationCode);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setText(confirmationCode);
        mailSender.send(message);
        log.info("[x] Confirmation send successfully");
    }
}
