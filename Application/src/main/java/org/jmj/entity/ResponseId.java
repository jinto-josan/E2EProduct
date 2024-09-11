package org.jmj.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ResponseId implements Serializable {
    @Embedded
    @lombok.NonNull
    private RequestId requestId;

    @lombok.NonNull
    @Enumerated(value = EnumType.STRING)
    private  org.springframework.http.HttpStatus statusCode;

    @Enumerated(EnumType.STRING)
    @lombok.NonNull
    private ResponseType type;

}
