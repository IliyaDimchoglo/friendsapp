package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.dto.request.AccessRequest;
import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.service.AccessDBService;
import com.skysoft.business.api.service.AccessService;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final ConfirmationService confirmationService;
    private final AccessDBService accessDBService;
    private final AccountService accountService;

    @Override
    public void createAccessRequest(AccessRequest request) {
        log.info("[x] Account registration request: {}", request);
        if (!isEmailAndUsernameExistAndConfirmed(request.getEmail(), request.getUsername())) {
            AccessRequestEntity entity = accessDBService.save(request.toEntity());
            confirmationService.sendConfirmation(entity.getEmail(), entity.getId(), entity.getConfirmationCode());
        }else {
            throw new BadRequestException("Email already confirmed.");
        }
    }

    @Override
    public void confirmAccess(UUID accessId, UUID confirmationCode) {
        AccessRequestEntity entity = accessDBService.findByIdAndConfirmationCode(accessId, confirmationCode);
        entity.setConfirmed(true);
        AccountEntity accountEntity = accountService.registerNewAccount(entity);
        accessDBService.confirmAccessRequest(entity, accountEntity);
        log.info("[x] Access successfully confirmed for email: {}.", entity.getEmail());
    }

    private boolean isEmailAndUsernameExistAndConfirmed(String email, String username){
        return accessDBService.existConfirmedEmailAndUsername(email, username);// FIXME: 23.01.20
    }
}
