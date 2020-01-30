package com.skysoft.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.Instant;

@Data
@Embeddable
@NoArgsConstructor
public class Avatar {

    private String url;

    private Instant createdTime;

    public Avatar(String url) {
        this.url = url;
        createdTime = Instant.now();
    }
}
