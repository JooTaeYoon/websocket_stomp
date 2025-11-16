package com.kicc.app.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clothes")
public class ClothesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "clothes")
    private String clothes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;
}
