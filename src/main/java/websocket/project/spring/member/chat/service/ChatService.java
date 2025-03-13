package websocket.project.spring.member.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websocket.project.spring.member.chat.domain.ChatMessage;
import websocket.project.spring.member.chat.domain.ChatParticipant;
import websocket.project.spring.member.chat.domain.ChatRoom;
import websocket.project.spring.member.chat.domain.ReadStatus;
import websocket.project.spring.member.chat.dto.ChatMessageReqDto;
import websocket.project.spring.member.chat.repository.ChatMessageRepository;
import websocket.project.spring.member.chat.repository.ChatParticipantRepository;
import websocket.project.spring.member.chat.repository.ChatRoomRepository;
import websocket.project.spring.member.chat.repository.ReadStatusRepository;
import websocket.project.spring.member.domain.Member;
import websocket.project.spring.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MemberRepository memberRepository;

    public ChatService(ChatRoomRepository chatRoomRepository, ChatParticipantRepository chatParticipantRepository, ChatMessageRepository chatMessageRepository, ReadStatusRepository readStatusRepository, MemberRepository memberRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.readStatusRepository = readStatusRepository;
        this.memberRepository = memberRepository;
    }


    public void saveMessage(Long roomId, ChatMessageReqDto chatMessageReqDto) {
//        채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() ->
                new EntityNotFoundException("room cannot find !")
        );
//        보낸 사람 조회
        Member sender = memberRepository.findByEmail(chatMessageReqDto.getSenderEmail()).orElseThrow(() -> new EntityNotFoundException("이메일어 없네"));

//        메세지 저장
        ChatMessage chatMessage = ChatMessage.builder().
                chatRoom(chatRoom).member(sender)
                .content(chatMessageReqDto.getMessage())
                .build();
//        chatRoomRepository.save(chatRoom);
        chatMessageRepository.save(chatMessage);

//        사용자별로 읽음여부 저장
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        for (ChatParticipant c : chatParticipants) {
            ReadStatus readStatus = ReadStatus.builder().
                    chatRoom(chatRoom)
                    .member(c.getMember())
                    .chatMessage(chatMessage)
                    .isRead(c.getMember().equals(sender))
                    .build();

            readStatusRepository.save(readStatus);
        }
    }

    public void createGroupRoom(String chatRoomName) {
        log.info("이거 뭐야 : " + SecurityContextHolder.getContext().getAuthentication().getName());
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("이메일이 없네"));

//        채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder().
                name(chatRoomName).isGroupChat("Y")
                .build();

//        채팅 참여자로 개설자를 추가
        chatRoomRepository.save(chatRoom);

        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom).member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

}
