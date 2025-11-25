package com.kicc.app.dto.response;

import com.kicc.app.dto.request.CustomerDtoRequest;
import com.kicc.app.entity.CustomerEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomerDtoResponse {

    private String name;

    public static CustomerDtoResponse response(CustomerDtoRequest result){
        return CustomerDtoResponse.builder().name(result.getName()).build();
    }


}
