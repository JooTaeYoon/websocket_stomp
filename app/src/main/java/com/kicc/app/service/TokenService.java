package com.kicc.app.service;

import com.kicc.domain.client.ClientFeign;
import com.kicc.domain.dto.ApiDto;
import com.kicc.domain.dto.SendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final ClientFeign clientFeign;
    private final CacheManager cacheManager;

    @Cacheable(cacheNames = "accessTokenCache", key = "'token'")
    public ApiDto getToken() {
        ResponseEntity<ApiDto> token = clientFeign.getToken();
        log.info("토큰 >>> {}", token);
        return token.getBody();
    }

    public ResponseEntity<?> send() {
        ApiDto token = getToken();
        String authHeader = "Bearer " + token.getAccessToken();
        SendRequest sendRequest = new SendRequest();
        sendRequest.setName("이름테스트");
        sendRequest.setPassword("1234");
        Map<String, Object> send = clientFeign.send(authHeader, sendRequest);
        log.info("send >>> {}", send);
        return ResponseEntity.ok(send);
    }

    public void tokenCheck() {
        log.info("token check >>> {}", cacheManager.getCache("accessTokenCache").get("token", Object.class));
    }
}
