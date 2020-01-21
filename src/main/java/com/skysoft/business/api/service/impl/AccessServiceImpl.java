package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.dto.request.AccessRequest;
import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.service.AccessDBService;
import com.skysoft.business.api.service.AccessService;
import com.skysoft.business.api.service.AccountService;
import com.skysoft.business.api.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        AccessRequestEntity entity = accessDBService.save(request.toEntity());
        confirmationService.sendConfirmation(entity.getEmail(), entity.getConfirmationCode());
    }

    @Override
    public void confirmAccess(String email, String confirmationCode) {
        AccessRequestEntity entity = accessDBService.findByEmailAndConfirmationCode(email, confirmationCode)
                .orElseThrow(() -> new NotFoundException("email or confirmationCode not valid"));
        entity.setConfirmed(true);
        AccountEntity accountEntity = accountService.registerNewAccount(entity);
        accessDBService.confirmAccessRequest(entity, accountEntity);
        log.info("[x] Access successfully confirmed with email: {}", email);
    }

}
