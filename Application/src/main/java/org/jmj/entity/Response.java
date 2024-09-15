package org.jmj.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jmj.annotations.FqdnValidator;
import org.jmj.entity.deserializers.HttpStatusCodeDeserializer;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@FqdnValidator
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @lombok.NonNull
    @Enumerated(value = EnumType.STRING)
    @JsonDeserialize(using = HttpStatusCodeDeserializer.class)
    private  org.springframework.http.HttpStatus statusCode;

    @Enumerated(EnumType.STRING)
    @lombok.NonNull
    private ResponseType type;

    @Lob
    @JsonRawValue
    private String body;
    @Lob
    private String headers;

    private String fqdn;

    @lombok.NonNull
    private Integer responseOrder;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "path", referencedColumnName = "path"),
            @JoinColumn(name = "method", referencedColumnName = "method"),
            @JoinColumn(name = "sub_system_id", referencedColumnName = "sub_system_id")
    })
    @JsonUnwrapped
    @lombok.NonNull
    private Request request;


//    @PrePersist
//    @PreUpdate
//    private void setRequest() {
//        if (request != null) {
//            this.id.setRequestId(request.getId());
//        }
//    }

    //used for setting body while deserializing
    @JsonSetter("body")
    public void jsonBodySetter(JsonNode body) {
        this.body = body.toString();
    }


    //Todo: Unique Constraint for type rest and status

}