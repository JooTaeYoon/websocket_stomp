package websocket.project.spring.member.chat.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 스프링과 stomp는 기본적으로 세션관리를 자동으로 처리 (내부적으로 처리 함)
// 연결 및 해제 이벤트를 기록, 영ㄴ결된 세션수를 실시간으로 확인할 목적으로 이벤트 리스너를 생성 => 로그 , 디버기 목적
@Component
@Slf4j
public class StompEventListener {

    private final Set<String> sessions =  ConcurrentHashMap.newKeySet();

    @EventListener
    public void connectHandle(SessionConnectedEvent event) {
        log.info("connectHandle "+event.toString());
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.add(accessor.getSessionId());
        log.info("세션 ID: " + accessor.getSessionId());
        log.info("토탈 세션 " + sessions.size());
    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) {
        log.info("디스커넥트 "+event.toString());
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sessions.remove(accessor.getSessionId());
        log.info("세션 ID: " + accessor.getSessionId());
        log.info("토탈 세션 " + sessions.size());
    }


}
