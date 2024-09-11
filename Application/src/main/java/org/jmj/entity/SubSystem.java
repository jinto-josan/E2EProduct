package org.jmj.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jmj.converters.LowerCaseConverter;

import java.util.Set;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
//table created so that if in future we want to add more fields to the sub system we can do it easily
public class SubSystem {
    @Id
    @lombok.NonNull
    private String name;

    private String description;
//need to ensure azure access to MI is there for the sub system other than controller
//    @Enumerated(EnumType.STRING)
//    private SubSystemType type = SubSystemType.REST;

    @OneToMany(mappedBy = "subSystem", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Request> requests;
    // Getters and Setters by lombok


    public void setName(String name) {
        this.name = name.toLowerCase();
    }


}