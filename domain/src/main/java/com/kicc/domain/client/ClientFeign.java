package com.kicc.domain.client;


import com.kicc.domain.dto.ApiDto;
import com.kicc.domain.dto.SendRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Map;

@FeignClient(url = "http://localhost:8081", name = "feignClient")
public interface ClientFeign {

    @PostMapping(value = "/api/send", consumes = "application/json")
    Map<String, Object> send(@RequestHeader(HttpHeaders.AUTHORIZATION)String header, @RequestBody SendRequest request);

    @PostMapping("/api/token")
    public ResponseEntity<ApiDto> getToken();

}
