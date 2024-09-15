package org.jmj.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jmj.converters.LowerCaseConverter;

import java.io.Serializable;

@Embeddable
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class RequestId implements Serializable {
    @lombok.NonNull
    @Convert(converter = LowerCaseConverter.class)
    private String path;
    @lombok.NonNull
    @Enumerated(EnumType.STRING)
    //Because there was no enumerable available in spring
    private HttpMethod method;
    @lombok.NonNull
    @Column(name = "sub_system_id")
    private String subSystemId;

}
