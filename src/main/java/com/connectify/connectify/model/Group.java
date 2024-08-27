package com.connectify.connectify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

//    @Column(name = "description", nullable = false)
//    private String name;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "name", nullable = false)
//    private String name;
//
//    @Column(name = "name", nullable = false)
//    private String name;
}
