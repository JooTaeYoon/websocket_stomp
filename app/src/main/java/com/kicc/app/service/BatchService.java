package com.kicc.app.service;

import com.kicc.app.dto.response.CustomerDtoResponse;
import com.kicc.app.entity.CustomerEntity;
import com.kicc.app.repository.ClothesRepository;
import com.kicc.app.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final CustomerRepository customerRepository;
    private final ClothesRepository clothesRepository;

    public ResponseEntity<CustomerDtoResponse> saveCustomer() {



        return null;
    }

}
