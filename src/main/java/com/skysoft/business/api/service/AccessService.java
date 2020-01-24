package com.skysoft.business.api.service;

import com.skysoft.business.api.dto.request.AccessRequest;

import java.util.UUID;

public interface AccessService {

    void createAccessRequest(AccessRequest request);

    void confirmAccess(UUID accessId, UUID confirmationCode);
}
