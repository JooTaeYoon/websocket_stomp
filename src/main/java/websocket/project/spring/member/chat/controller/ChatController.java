package websocket.project.spring.member.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import websocket.project.spring.member.chat.service.ChatService;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

//    그룹채팅방 개설
    @PostMapping("/room/group/create")
    public ResponseEntity<?> createGroupRoom(@RequestParam String roomName){
        chatService.createGroupRoom(roomName);
        return ResponseEntity.ok().build();
    }
}
