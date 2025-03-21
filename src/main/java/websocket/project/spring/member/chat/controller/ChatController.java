package websocket.project.spring.member.chat.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import websocket.project.spring.member.chat.dto.ChatMessageDto;
import websocket.project.spring.member.chat.dto.ChatRoomListResDto;
import websocket.project.spring.member.chat.dto.MyChatListResDto;
import websocket.project.spring.member.chat.service.ChatService;

import java.util.HashMap;
import java.util.List;

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

//    그룹 채팅 목록 조회
    @GetMapping("/room/group/list")
    public ResponseEntity<?> getGroupChatRooms() {
        List<ChatRoomListResDto> chatRooms= chatService.getGroupChatRooms();
        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }


    //    그룹채팅방 참여
    @PostMapping("/room/group/{roomId}/join")
    public ResponseEntity<?> joinGroupChatRoom(@PathVariable Long roomId) {
        chatService.addParticipantToGroupChat(roomId);
        return ResponseEntity.ok().build();
    }

    //    이전 메시지 조회
    @GetMapping("/history/{roomId}")
    public ResponseEntity<?> getChatHistory(@PathVariable Long roomId) throws IllegalAccessException {
        List<ChatMessageDto> chatMessageDtos = chatService.getChatHistory(roomId);
//        return ResponseEntity.ok().body(chatMessageDtos);
        return new ResponseEntity<>(chatMessageDtos, HttpStatus.OK);
    }

    //    채팅메시지 읽음처리
    @PostMapping("/room/{roomId}/read")
    public ResponseEntity<?> messageRead(@PathVariable Long roomId) {
        chatService.messageRead(roomId);
        return ResponseEntity.ok().build();
    }

    //    내 채팅방 목록 조회: roomId, roomName, 그룹채팅 여부, 메세지 읽음 갯수

    @GetMapping("/my/rooms")
    public ResponseEntity<?> getMyChatRooms() {
        List<MyChatListResDto> myChatListResDtos= chatService.getMyChatRooms();
        return new ResponseEntity<>(myChatListResDtos, HttpStatus.OK);
    }

//    채팅방 나가기
    @DeleteMapping("/room/group/{roomId}/leave")
    public ResponseEntity<?> leaveGroupChatRoom(@PathVariable Long roomId) throws IllegalAccessException {
        chatService.leaveGroupChatRoom(roomId);
        return ResponseEntity.ok().build();
    }

}
