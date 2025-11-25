package com.kicc.app.service;

import com.kicc.app.dto.request.ClothesDtoRequest;
import com.kicc.app.dto.response.ClothesDtoResponse;
import org.springframework.http.ResponseEntity;

public interface ClothesService {

    ClothesDtoResponse saveClothes(ClothesDtoRequest request);


}
