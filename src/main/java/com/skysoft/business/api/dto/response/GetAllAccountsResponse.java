package com.skysoft.business.api.dto.response;

import com.skysoft.business.api.dto.AccountDetailsDto;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class GetAllAccountsResponse {

    List<AccountDetailsDto> accounts;
}
