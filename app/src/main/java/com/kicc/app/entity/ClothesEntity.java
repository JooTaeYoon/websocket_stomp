package com.kicc.app.entity;

import com.kicc.app.dto.request.CustomerDtoRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clothes")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
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
