package org.jmj.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jmj.converters.LowerCaseConverter;

import java.io.Serializable;
import java.util.Objects;

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
    private HttpMethod method;
    @lombok.NonNull
    @Column(name = "sub_system_id")
    private String subSystemId;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        RequestId requestId = (RequestId) o;
//        return path.equals(requestId.path) && method.equals(requestId.method) && Objects.equals(subSystem, requestId.subSystem);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(path, method, subSystem);
//    }
}
