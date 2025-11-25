package com.kicc.app.controller;

import com.kicc.app.service.TokenService;
import com.kicc.domain.client.ClientFeign;
import com.kicc.domain.dto.ApiDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class FeignController {

    private final ClientFeign clientFeign;

    private final TokenService tokenService;

    @GetMapping("/get/token")
    public ResponseEntity<?> test() {
        ApiDto token = tokenService.getToken();
        log.info("token >>> {}", token);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/token_test")
    public void tokenCheck() {
        tokenService.tokenCheck();
    }

    @GetMapping("/get/login")
    public ResponseEntity<?> getToken() {
        ResponseEntity<?> send = tokenService.send();
        return ResponseEntity.ok(send);
    }

}
