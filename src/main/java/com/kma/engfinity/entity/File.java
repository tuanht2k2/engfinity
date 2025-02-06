package com.kma.engfinity.entity;

import com.kma.common.entity.Account;
import com.kma.engfinity.enums.EFileType;
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
@Table(name = "files")
public class File  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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
    private Date updatedAt;

    private String url;

    @Enumerated(EnumType.STRING)
    private EFileType type;
}
