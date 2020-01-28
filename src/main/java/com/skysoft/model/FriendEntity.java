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
    @JoinColumn(name = "account_1_id")
    private AccountEntity account1;// FIXME: 28.01.20

    @ManyToOne
    @JoinColumn(name = "account_2_id")
    private AccountEntity account2;// FIXME: 28.01.20

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    public FriendEntity(AccountEntity account, AccountEntity friendAccount) {
        status = FriendStatus.ACTIVE;
        this.account2 = friendAccount;
        this.account1 = account;
    }

    public void delete() {
        status = FriendStatus.DELETED;
    }

    public AccountEntity getMyFriend(String username){
        if(account1.getUsername().equals(username)){
            return account2;
        }else {
            return account1;
        }
    }

}
