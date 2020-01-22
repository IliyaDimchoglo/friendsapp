package com.skysoft.business.api.service;

import com.skysoft.business.api.dto.request.AccessRequest;

public interface AccessService {

    void createAccessRequest(AccessRequest request);

    void confirmAccess(String confirmationCode);
}
