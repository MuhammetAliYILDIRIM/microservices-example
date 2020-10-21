package com.may.accountservice.repository.entity;

import com.may.accountservice.util.validator.Email;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullName;

    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean isActive;

}
