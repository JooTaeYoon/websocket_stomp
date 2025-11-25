package com.kicc.app.service.impl;

import com.kicc.app.dto.request.CustomerDtoRequest;
import com.kicc.app.dto.response.CustomerDtoResponse;
import com.kicc.app.entity.CustomerEntity;
import com.kicc.app.repository.CustomerRepository;
import com.kicc.app.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;


    @Override
    public CustomerDtoResponse saveCustomer(CustomerDtoRequest customerDto) {

        Optional<CustomerEntity> findCustomer = customerRepository.findByNameAndPhoneNumber(customerDto.getName(), customerDto.getPhoneNumber());

//        손님이 없으면 저장
        if (findCustomer.isEmpty()) {
            CustomerEntity customer = CustomerEntity.createCustomer(customerDto);
            CustomerEntity save = customerRepository.save(customer);
            log.info("save >>> {}", save);
            return CustomerDtoResponse.response(customerDto);
        } else {
            return CustomerDtoResponse.response(customerDto);
        }
    }

}
