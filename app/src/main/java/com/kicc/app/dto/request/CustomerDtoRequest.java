package com.kicc.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kicc.app.entity.ClothesEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDtoRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

}
