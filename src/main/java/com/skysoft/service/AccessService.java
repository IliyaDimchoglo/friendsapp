package com.skysoft.service;

import com.skysoft.dto.request.AccessRequest;

import java.util.UUID;

public interface AccessService {

    void createAccessRequest(AccessRequest request);

    void confirmAccess(UUID accessId, UUID confirmationCode);
}
