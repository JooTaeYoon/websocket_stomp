package com.kicc.app.service;

import com.kicc.app.dto.request.CustomerDtoRequest;
import com.kicc.app.dto.response.CustomerDtoResponse;

public interface CustomerService {

     CustomerDtoResponse saveCustomer(CustomerDtoRequest customerDto);


}
