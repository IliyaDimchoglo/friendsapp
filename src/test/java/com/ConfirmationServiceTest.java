package com;

import com.skysoft.service.impl.ConfirmationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmationServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Spy
    @InjectMocks
    private ConfirmationServiceImpl confirmationService;


    @Before
    public void setUp() {


    }

    @Test
    public void sendConfirmationTest() {
        String email = randomUUID().toString();
        String text = randomUUID().toString();
        UUID id = randomUUID();
        UUID confCode = randomUUID();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText(text);
        message.setTo(email);
        message.setFrom(randomUUID().toString());
        doNothing().when(javaMailSender).send(message);

        confirmationService.sendConfirmation(email, id, confCode);

        verify(confirmationService, times(1)).sendConfirmation(email, id, confCode);
    }

}
