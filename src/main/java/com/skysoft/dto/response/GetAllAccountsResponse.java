package com.skysoft.dto.response;

import com.skysoft.dto.AccountDto;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class GetAllAccountsResponse {

    private List<AccountDto> accounts;
}
