package com.skysoft.model;

import com.skysoft.dto.request.UpdateAccountRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @CreationTimestamp
    private Instant createdTime;

    @UpdateTimestamp
    private Instant updatedTime;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    private byte[] avatar;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String phoneNumber;

    private String address;

    public boolean update(UpdateAccountRequest request) {
        boolean updated = false;
        if (!StringUtils.isEmpty(request.getFirstName()) && !request.getFirstName().equals(firstName)) {
            this.firstName = request.getFirstName();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getLastName()) && !request.getLastName().equals(lastName)) {
            this.lastName = request.getLastName();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getEmail()) && !request.getEmail().equals(email)) {
            this.email = request.getEmail();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getPhoneNumber()) && !request.getPhoneNumber().equals(phoneNumber)) {
            this.phoneNumber = request.getPhoneNumber();
            updated = true;
        }
        if (!StringUtils.isEmpty(request.getAddress()) && !request.getAddress().equals(address)) {
            this.address = request.getAddress();
            updated = true;
        }
        return updated;
    }


}
