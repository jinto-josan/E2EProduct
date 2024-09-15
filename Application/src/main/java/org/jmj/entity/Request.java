package org.jmj.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Request {
    @EmbeddedId
    @lombok.NonNull
    @JsonUnwrapped
    private RequestId id;

    @Lob
    private String body;
    @Lob
    private String headers;

    @ManyToOne
    @JoinColumn(name = "sub_system_id", referencedColumnName = "name", insertable = false, updatable = false)
    @lombok.NonNull
    private SubSystem subSystem;

    //orphanRemoval is kept as false because if a request is deleted, the response should not be deleted and the
    //controlling party is the response.
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL)
    private Set<Response> responses;

    public void setSubSystem(SubSystem subSystem) {
        this.subSystem = subSystem;
        this.id.setSubSystemId(subSystem.getName());
    }


}
