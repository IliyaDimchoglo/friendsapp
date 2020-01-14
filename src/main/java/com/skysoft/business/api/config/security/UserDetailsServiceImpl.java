package com.skysoft.business.api.config.security;

import com.skysoft.business.api.exception.RegistrationException;
import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.service.AccessDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccessDBService accessService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        AccessRequestEntity accessRequestEntity = accessService.getByName(username);
        if (accessRequestEntity.isConfirmed()) {
            return new CustomUserDetails(accessRequestEntity.getUsername(), accessRequestEntity.getPassword());
        } else
            log.error("[x] Provided name was already confirmed: {}.", username);
        throw new RegistrationException("Provided access id was already confirmed");

    }

}
