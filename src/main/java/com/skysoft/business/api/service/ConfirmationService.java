package com.skysoft.business.api.service;

public interface ConfirmationService {

    void sendConfirmation(String email, String confirmationCode);
}
