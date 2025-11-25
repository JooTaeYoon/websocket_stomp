package com.kicc.app.controller;

import com.kicc.app.dto.request.ClothesDtoRequest;
import com.kicc.app.dto.request.CustomerDtoRequest;
import com.kicc.app.service.ClothesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {

    private final ClothesService clothesService;

    @PostMapping("/save_clothes")
    public ResponseEntity<?> saveClothes(@RequestBody ClothesDtoRequest request){
        clothesService.saveClothes(request);
        return ResponseEntity.ok("");
    }
}
