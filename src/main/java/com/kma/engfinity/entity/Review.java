package com.kma.engfinity.entity;

import com.kma.common.entity.Account;
import com.kma.engfinity.enums.EReviewType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(
        name = "reviews",
        indexes = {
                @Index(name = "idx_reviewed_account", columnList = "reviewed_account"),
                @Index(name = "idx_reviewed_topic", columnList = "reviewed_topic"),
                @Index(name = "idx_created_by", columnList = "created_by"),
                @Index(name = "idx_updated_by", columnList = "updated_by")
        }
)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EReviewType type;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @ManyToOne
    @JoinColumn(name = "reviewed_account")
    private Account reviewedAccount;

    @ManyToOne
    @JoinColumn(name = "reviewed_topic")
    private Topic reviewedTopic;

    private Integer rating;

    private String content;
}
