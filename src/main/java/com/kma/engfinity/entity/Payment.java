package com.kma.engfinity.entity;

import com.kma.engfinity.enums.EPaymentType;
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
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account updatedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EPaymentType type;

    private Long amount;

    private Long remainingBalance;

    private String description;
}
