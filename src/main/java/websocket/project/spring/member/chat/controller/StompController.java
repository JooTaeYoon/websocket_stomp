package websocket.project.spring.member.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class StompController {

    @MessageMapping("/{roomId}")    // 클라이언트에서 특정 publish/roomId 형태로 메시지를 발행 시, MessageMapping으로 수신
    @SendTo("/topic/{roomId}")      // 해당 roomId로 메세지 전달 하여 구독 중인 클라이언트에게 메세지 전송
//    DestinationVariable: @MessageMapping 어노테이션으로 정의된 WebSocket Controller내에서만 사용
    public String sendMessage(@DestinationVariable Long roomId , String message) {
        log.info("message: "+message);
        return message;
    }

}
