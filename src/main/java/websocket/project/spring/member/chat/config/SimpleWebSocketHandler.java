package websocket.project.spring.member.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// connect로 웹소켓 연결요청이 들어 왔을 때, 이를 처리할 클래스
@Component
@Slf4j
public class SimpleWebSocketHandler extends TextWebSocketHandler {

//    연결된 세션 관리 : 스레드 safe한 set 사용
    private final Set<WebSocketSession> sessions= ConcurrentHashMap.newKeySet();

//    연결이 된 후 실행 되는 함수
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("연결이 된 후 실행 되는 함수  "+session.getId());

        log.info(String.valueOf(session.getHandshakeHeaders()));

    }


//    사용자한테 메세지를 보내주는 함수
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("사용자한테 메세지를 보내주는 함수 "+payload);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

//    연결이 종료 된 후 실행 되는 함수
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("연결이 종료 된 후 실행 되는 함수 ",session.toString());
        sessions.remove(session);
    }


}
