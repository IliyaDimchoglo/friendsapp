package com.skysoft.business.api.dto.response;

import com.skysoft.business.api.dto.AccountDto;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class GetAllAccountsResponse {

    private List<AccountDto> accounts;
}
