package org.jmj.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;
import lombok.*;
import org.jmj.annotations.FqdnValidator;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@FqdnValidator
public class Response {
    @lombok.NonNull
    @EmbeddedId
    private ResponseId id;
//Todo: Only one Rest response to be there for a request and no other Rest response should be there for the same request
    //Todo: Multiple request for same type to be allowed
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
            @JoinColumn(name = "path", referencedColumnName = "path",insertable = false, updatable = false),
            @JoinColumn(name = "method", referencedColumnName = "method",insertable = false, updatable = false),
            @JoinColumn(name = "sub_system_id", referencedColumnName = "sub_system_id", insertable = false, updatable = false)
    })
    @lombok.NonNull
    private Request request;


    @PrePersist
    @PreUpdate
    private void setRequest() {
        if (request != null) {
            this.id.setRequestId(request.getId());
        }
    }


    //Todo: Unique Constraint for type rest and status

    // Getters and Setters
}