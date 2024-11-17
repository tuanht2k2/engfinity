package com.connectify.connectify.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CommentReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
