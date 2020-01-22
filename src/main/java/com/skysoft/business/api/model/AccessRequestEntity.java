package com.skysoft.business.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccessRequestEntity  extends BaseUpdatedEntity{

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column (nullable = false)
    private String password;

    @Column(unique = true)
    private String confirmationCode;

    private boolean confirmed;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    public boolean isConfirmed() {
        return Objects.nonNull(account);
    }
}
