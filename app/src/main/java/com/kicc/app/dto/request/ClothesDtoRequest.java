package com.kicc.app.dto.request;

import com.kicc.app.entity.ClothesEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ClothesDtoRequest {

    private String clothes;
    private String name;

    private String phoneNumber;

    public static ClothesEntity clothesEntity(String clothes){
        ClothesEntity clothesEntity = new ClothesEntity();
        return clothesEntity;
    }

}
