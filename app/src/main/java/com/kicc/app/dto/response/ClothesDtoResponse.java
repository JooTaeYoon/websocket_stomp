package com.kicc.app.dto.response;

import com.kicc.app.entity.ClothesEntity;
import com.kicc.app.entity.CustomerEntity;
import lombok.*;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClothesDtoResponse {

    private String clothes;

    private CustomerDtoResponse customer;

    public static ClothesDtoResponse response(ClothesEntity clothesEntity) {
        return ClothesDtoResponse.builder().build();
    }

}
