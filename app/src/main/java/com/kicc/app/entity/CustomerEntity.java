package com.kicc.app.entity;

import com.kicc.app.dto.request.CustomerDtoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "customer")
@Table(name = "customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    public static CustomerEntity createCustomer(CustomerDtoRequest request) {
        return CustomerEntity.builder().name(request.getName()).phoneNumber(request.getPhoneNumber()).build();
    }


}
