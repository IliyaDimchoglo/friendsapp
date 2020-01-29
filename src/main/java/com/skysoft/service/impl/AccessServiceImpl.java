package com.skysoft.service.impl;

import com.skysoft.dto.request.AccessRequest;
import com.skysoft.exception.BadRequestException;
import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import com.skysoft.service.AccessDBService;
import com.skysoft.service.AccessService;
import com.skysoft.service.AccountService;
import com.skysoft.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final ConfirmationService confirmationService;
    private final AccessDBService accessDBService;
    private final AccountService accountService;

    @Override
    @Transactional
    public void createAccessRequest(AccessRequest request) {
        String email = request.getEmail();
        String username = request.getUsername();
        log.info("[x] Account registration request: {}", request);
        boolean isConfirmed = accessDBService.existConfirmedEmailAndUsername(email, username);
        if (!isConfirmed) {
            AccessRequestEntity entity = accessDBService.save(request.toEntity());
            confirmationService.sendConfirmation(entity.getEmail(), entity.getId(), entity.getConfirmationCode());
        } else {
            throw new BadRequestException("Email already confirmed.");
        }
    }

    @Override
    @Transactional
    public void confirmAccess(UUID accessId, UUID confirmationCode) {
        AccessRequestEntity accessRequestEntity = accessDBService.findByIdAndConfirmationCode(accessId, confirmationCode);
        AccountEntity accountEntity = accountService.registerNewAccount(accessRequestEntity);
        accessRequestEntity.setAccount(accountEntity);
        log.info("[x] Access successfully confirmed for email: {}.", accessRequestEntity.getEmail());
    }

}
