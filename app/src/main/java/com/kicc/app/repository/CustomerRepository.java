package com.kicc.app.repository;

import com.kicc.app.dto.request.CustomerDtoRequest;
import com.kicc.app.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {


    List<CustomerEntity> findByName(String name);

    Optional<CustomerEntity> findByNameAndPhoneNumber(String name, String phoneNumber);
}
