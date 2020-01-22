package com.skysoft.business.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

import static com.skysoft.business.api.model.InviteStatus.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InviteEntity extends BaseCreatedEntity{

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private AccountEntity friend;

    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    public static InviteEntity of(AccountEntity accountId, AccountEntity friendId, InviteStatus inviteStatus){
        return new InviteEntity(accountId,friendId, inviteStatus);
    }
}
