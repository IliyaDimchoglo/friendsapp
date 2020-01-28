package com.skysoft.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skysoft.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class GetAllInvitationsResponse {

    @JsonProperty("incoming_invites")
    private List<AccountDto> incomingInvitesList;


    @JsonProperty("outgoing_invites")
    private List<AccountDto> outgoingInvitesList;
}
