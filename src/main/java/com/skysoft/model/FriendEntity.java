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
public class FriendEntity extends BaseUpdatedEntity {

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private AccountEntity friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public FriendEntity(AccountEntity account, AccountEntity friendAccount) {
        status = FriendStatus.ACTIVE;
        this.friend = friendAccount;
        this.account = account;
    }

    public void delete() {
        status = FriendStatus.DELETED;
    }

}
