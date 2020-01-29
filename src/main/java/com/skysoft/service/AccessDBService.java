package com.skysoft.service;


import com.skysoft.exception.NotFoundException;
import com.skysoft.model.AccessRequestEntity;

import java.util.Optional;
import java.util.UUID;

public interface AccessDBService {

    AccessRequestEntity save(AccessRequestEntity entity);

    Optional<AccessRequestEntity> getOptionalByUsername(String username);

    default AccessRequestEntity getByName(String username) throws NotFoundException {
        return getOptionalByUsername(username)
                .orElseThrow(() -> new NotFoundException("Access request with provided name was not found."));
    }

    Optional<AccessRequestEntity> findOptionalByIdAndConfirmationCode(UUID accessId, UUID confirmationCode);

    default AccessRequestEntity findByIdAndConfirmationCode(UUID accessId, UUID confirmationCode){
        return findOptionalByIdAndConfirmationCode(accessId, confirmationCode).orElseThrow(
                ()-> new NotFoundException("Access not found."));
    }

    boolean existConfirmedEmailAndUsername(String email, String username);

}
