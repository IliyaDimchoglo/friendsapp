package com;

import com.skysoft.dto.request.AccessRequest;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import com.skysoft.service.AccessDBService;
import com.skysoft.service.AccountService;
import com.skysoft.service.ConfirmationService;
import com.skysoft.service.impl.AccessServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.UUID.randomUUID;
import static org.junit.gen5.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccessServiceTest {

    @Mock
    private ConfirmationService confirmationService;

    @Mock
    private AccessDBService accessDBService;

    @Mock
    private AccountService accountService;

    @Spy
    @InjectMocks
    private AccessServiceImpl accessService;

    private String email;
    private String username;
    private String password;

    @Before
    public void setUp() {
        email = "iliya1292.d@gmail.com";
        username = "iliya";
        password = "password";
    }

    @Test
    public void successfulCreateAccessRequestTest() {
        AccessRequest accessRequest = new AccessRequest(email, username, password);
        AccessRequestEntity entity = accessRequest.toEntity();
        when(accessDBService.save(any())).thenReturn(entity);
        doNothing().when(confirmationService).sendConfirmation(entity.getEmail(), entity.getId(), entity.getConfirmationCode());

        accessService.createAccessRequest(accessRequest);

        verify(accessDBService, times(1)).save(any());
        verify(confirmationService, times(1)).sendConfirmation(entity.getEmail(), entity.getId(), entity.getConfirmationCode());
    }

    @Test
    public void failCreateAccessRequestTest() {
        when(accessDBService.existConfirmedEmailAndUsername(anyString(), anyString())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> accessService.createAccessRequest(new AccessRequest(randomUUID().toString(), randomUUID().toString(), randomUUID().toString())));
    }

    @Test
    public void successfulConfirmAccess(){
        AccessRequest accessRequest = new AccessRequest(email, username, password);
        AccessRequestEntity entity = accessRequest.toEntity();
        AccountEntity accountEntity = entity.toAccountEntity();
        when(accessDBService.findByIdAndConfirmationCode(any(), any())).thenReturn(entity);
        when(accountService.registerNewAccount(any())).thenReturn(accountEntity);
        doNothing().when(accessDBService).confirmAccessRequest(entity, accountEntity);

        accessService.confirmAccess(randomUUID(), randomUUID());

        verify(accessDBService, times(1)).findByIdAndConfirmationCode(any(), any());
        verify(accountService, times(1)).registerNewAccount(any());
        verify(accessDBService, times(1)).confirmAccessRequest(entity, accountEntity);
    }

    @Test
    public void failConfirmAccess() {
        assertThrows(Exception.class, ()-> accessService.confirmAccess(randomUUID(),randomUUID()));
    }
}
