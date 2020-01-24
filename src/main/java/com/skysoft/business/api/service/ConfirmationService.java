package com.skysoft.business.api.service;

import java.util.UUID;

public interface ConfirmationService {

    void sendConfirmation(String email, UUID id, UUID confirmationCode);
}
