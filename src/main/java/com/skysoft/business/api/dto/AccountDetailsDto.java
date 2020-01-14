package com.skysoft.business.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skysoft.business.api.model.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDetailsDto {

    private String username;
    private String email;
    private byte[] avatar;

    public static AccountDetailsDto of(AccountEntity accountEntity){
        return new AccountDetailsDto(
                accountEntity.getUsername(),
                accountEntity.getEmail(),
                accountEntity.getAvatar());
    }
}
