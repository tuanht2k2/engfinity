package com.connectify.connectify.model;

import com.connectify.connectify.enums.ERole;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "role")
public class Role {
    @Id
    private String id = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private ERole name;
}
