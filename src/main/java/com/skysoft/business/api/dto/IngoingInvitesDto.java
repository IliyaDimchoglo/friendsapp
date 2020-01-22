package com.skysoft.business.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IngoingInvitesDto {
    private List<AccountDto> invitesList;
}
