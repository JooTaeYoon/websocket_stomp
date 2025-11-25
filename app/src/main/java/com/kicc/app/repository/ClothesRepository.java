package com.kicc.app.repository;

import com.kicc.app.entity.ClothesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothesRepository extends JpaRepository<ClothesEntity, Long> {

}
