package com.may.accountservice.repository.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Address extends BaseEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String addressLineOne;

    private String addressLineTwo;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    private String postCode;


}
