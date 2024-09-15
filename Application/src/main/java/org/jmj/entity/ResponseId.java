package org.jmj.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jmj.entity.deserializers.HttpStatusCodeDeserializer;

import java.io.Serializable;

@Embeddable
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class ResponseId implements Serializable {
    @Embedded
    @lombok.NonNull
    @JsonUnwrapped
    private RequestId requestId;

    @lombok.NonNull
    @Enumerated(value = EnumType.STRING)
    @JsonDeserialize(using = HttpStatusCodeDeserializer.class)
    private  org.springframework.http.HttpStatus statusCode;

    @Enumerated(EnumType.STRING)
    @lombok.NonNull
    private ResponseType type;


}
