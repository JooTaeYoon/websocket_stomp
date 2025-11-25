package com.kicc.app.controller;

import com.kicc.app.dto.request.CustomerDtoRequest;
import com.kicc.app.dto.response.CustomerDtoResponse;
import com.kicc.app.exception.AllException;
import com.kicc.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/save_customer")
    public ResponseEntity<CustomerDtoResponse> saveCustomer(@RequestBody CustomerDtoRequest customerDto)  {
        log.info("customerDto >>> {}", customerDto);

        if (customerDto == null) {
            throw new RuntimeException();
        }

        CustomerDtoResponse customerDtoResponse = customerService.saveCustomer(customerDto);
        return ResponseEntity.ok(customerDtoResponse);
    }
}
