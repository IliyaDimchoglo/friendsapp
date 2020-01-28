package com.skysoft.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InviteEntity extends BaseCreatedEntity {

    @ManyToOne
    @JoinColumn(name = "from_id")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "to_id")
    private AccountEntity friend;

    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    public static InviteEntity of(AccountEntity accountId, AccountEntity friendId, InviteStatus inviteStatus){
        return new InviteEntity(accountId,friendId, inviteStatus);
    }
}
