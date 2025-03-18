package websocket.project.spring.member.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import websocket.project.spring.member.chat.dto.ChatMessageDto;
import websocket.project.spring.member.chat.service.ChatService;

@Controller
@Slf4j
public class StompController {

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;

    public StompController(SimpMessageSendingOperations messageTemplate, ChatService chatService) {
        this.messageTemplate = messageTemplate;
        this.chatService = chatService;
    }


    //    방법1. MessageMapping(수신)과 SendTo(Topic에 메세지 전달) 한꺼번에 처리
//    @MessageMapping("/{roomId}")    // 클라이언트에서 특정 publish/roomId 형태로 메시지를 발행 시, MessageMapping으로 수신
//    @SendTo("/topic/{roomId}")      // 해당 roomId로 메세지 전달 하여 구독 중인 클라이언트에게 메세지 전송
//    //    DestinationVariable: @MessageMapping 어노테이션으로 정의된 WebSocket Controller내에서만 사용
//    public String sendMessage(@DestinationVariable Long roomId , String message) {
//        log.info("message: "+message);
//        return message;
//    }

//    방법2. MessageMapping 어노테이션만 활용.
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto message) {
        log.info("message: "+message+ " , "+"roomId: "+roomId);
        chatService.saveMessage(roomId, message);
        messageTemplate.convertAndSend("/topic/" + roomId, message);
    }
}
