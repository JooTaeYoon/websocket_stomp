package com.kicc.app.service.impl;

import com.kicc.app.dto.request.ClothesDtoRequest;
import com.kicc.app.dto.response.ClothesDtoResponse;
import com.kicc.app.entity.ClothesEntity;
import com.kicc.app.entity.CustomerEntity;
import com.kicc.app.repository.ClothesRepository;
import com.kicc.app.repository.CustomerRepository;
import com.kicc.app.service.ClothesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClothesServiceImpl implements ClothesService {

    private final ClothesRepository clothesRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ClothesDtoResponse saveClothes(ClothesDtoRequest request) {

        log.info("request >>> {}", request);

        Optional<CustomerEntity> byNameAndPhoneNumber = customerRepository.findByNameAndPhoneNumber(request.getName(), request.getPhoneNumber());
        log.info("{}",byNameAndPhoneNumber);
        if (byNameAndPhoneNumber.isPresent()) {
            ClothesEntity clothesEntity = ClothesEntity.builder().clothes(request.getClothes()).customer(byNameAndPhoneNumber.get()).build();
            log.info("clothesEntity >>> {}", clothesEntity);
            clothesRepository.save(clothesEntity);
            return ClothesDtoResponse.response(clothesEntity);
        }

        return null;
    }
}
