package com.kma.engfinity.entity;

import com.kma.engfinity.enums.EMessengerType;
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
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "messengers")
public class Messenger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private EMessengerType type;

    private String name;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    private Account createdBy;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private Account updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "accounts_messengers",
            joinColumns = @JoinColumn(name = "messenger_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> members = new HashSet<>();
}
