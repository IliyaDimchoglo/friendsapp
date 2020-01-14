package com.skysoft.business.api.service;


import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.model.AccountEntity;

import java.util.Optional;

public interface AccessDBService {

    AccessRequestEntity save(AccessRequestEntity entity);

    Optional<AccessRequestEntity> getOptionalByUsername(String username);

    default AccessRequestEntity getByName(String username) throws NotFoundException {
        return getOptionalByUsername(username)
                .orElseThrow(() -> new NotFoundException("Access request with provided name was not found"));
    }

    Optional<AccessRequestEntity> findByEmailAndConfirmationCode(String email, String confirmationCode);

    void confirmAccessRequest(AccessRequestEntity accessRequest, AccountEntity accountEntity);
}
