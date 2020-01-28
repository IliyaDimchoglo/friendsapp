package com.skysoft.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OutgoingInvitesDto {
    private List<AccountDto> invitesList;
}
