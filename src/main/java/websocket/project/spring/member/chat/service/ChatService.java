package websocket.project.spring.member.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import websocket.project.spring.member.chat.domain.ChatMessage;
import websocket.project.spring.member.chat.domain.ChatParticipant;
import websocket.project.spring.member.chat.domain.ChatRoom;
import websocket.project.spring.member.chat.domain.ReadStatus;
import websocket.project.spring.member.chat.dto.ChatMessageDto;
import websocket.project.spring.member.chat.dto.ChatRoomListResDto;
import websocket.project.spring.member.chat.dto.MyChatListResDto;
import websocket.project.spring.member.chat.repository.ChatMessageRepository;
import websocket.project.spring.member.chat.repository.ChatParticipantRepository;
import websocket.project.spring.member.chat.repository.ChatRoomRepository;
import websocket.project.spring.member.chat.repository.ReadStatusRepository;
import websocket.project.spring.member.domain.Member;
import websocket.project.spring.member.repository.MemberRepository;

import java.util.ArrayList;
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


    public void saveMessage(Long roomId, ChatMessageDto chatMessageReqDto) {
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

        log.info("SecurityContextHolder.getContext().getAuthentication().getName() : " + SecurityContextHolder.getContext().getAuthentication().getName());

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


    public List<ChatRoomListResDto> getGroupChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findByIsGroupChat("Y");
        List<ChatRoomListResDto> dtos = new ArrayList<>();
        for (ChatRoom c : chatRooms) {
            ChatRoomListResDto dto = ChatRoomListResDto.builder().
                    roomId(c.getId()).roomName(c.getName()).
                    build();
            dtos.add(dto);
        }
        return dtos;
    }

    public void addParticipantToGroupChat(Long roomId) {
//        채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("채팅방이 없네"));
//        member 조회
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("이메일이 또 없네"));
//        이미 참여자인지 검증
        Optional<ChatParticipant> participant = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member);

        if (!participant.isPresent()) {
            addParticipantToRoom(chatRoom, member);
        }
//        ChatParticipant 객체 생성 후 DB에 저장
    }

    public void addParticipantToRoom(ChatRoom chatRoom, Member member) {
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .member(member)
                .build();
        chatParticipantRepository.save(chatParticipant);
    }

    public List<ChatMessageDto> getChatHistory(Long roomId) throws IllegalAccessException {
//        내가 해당 채팅방의 참여자가 아닐 경우 에러
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() ->
                new EntityNotFoundException("너 해당방에 참여자 아니야"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException("너 해당방에 멤버가 아니야"));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        boolean check = false;

        for (ChatParticipant c : chatParticipants) {
            if (c.getMember().equals(member)) {
                check = true;
            }
        }
        if(!check) throw new IllegalAccessException("본인이 속하지 않은 채팅방입니다");

//        특정 room에 대한 message 조회
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomOrderByCreatedTimeAsc(chatRoom);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for (ChatMessage c: chatMessages){
            ChatMessageDto chatMessageDto = ChatMessageDto.builder().
            message(c.getContent()).senderEmail(c.getMember().getEmail()).
                    build();
            chatMessageDtos.add(chatMessageDto);
        }
        return chatMessageDtos;
    }


    public boolean isRoomParticipant(String email, Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("방 참여자가 아님"));
        log.info("룸참여자 email: "+email);
        log.info("뭐야 왜 안 됨?");
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("너도 아니야"));


        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);

        for (ChatParticipant c : chatParticipants) {
            if (c.getMember().equals(member)) {
                return true;
            }
        }
        return false;
    }

    public void messageRead(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("멤버 없음"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("멤버 낫"));
        List<ReadStatus> readStatuses = readStatusRepository.findByChatRoomAndMember(chatRoom, member);

        for (ReadStatus read : readStatuses) {
            read.updateIsRead(true);
        }
    }


    public List<MyChatListResDto> getMyChatRooms(){
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("멤버낫"));
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findAllByMember(member);
        List<MyChatListResDto> chatListResDtos = new ArrayList<>();

        for (ChatParticipant c : chatParticipants) {
            Long count = readStatusRepository.countByChatRoomAndMemberAndIsReadFalse(c.getChatRoom(), member);
            MyChatListResDto dto = MyChatListResDto.builder()
                    .roomId(c.getChatRoom().getId())
                    .roomName(c.getChatRoom().getName())
                    .isGroupChat(c.getChatRoom().getIsGroupChat())
                    .unReadCount(count)
                    .build();
            chatListResDtos.add(dto);
        }
        return chatListResDtos;
    }

    public void leaveGroupChatRoom(Long roomId) throws IllegalAccessException {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new EntityNotFoundException("없음"));
        Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new EntityNotFoundException("없음"));

        if (chatRoom.getIsGroupChat().equals("N")) {
            throw new IllegalAccessException("단체 채팅방이 아닙니다");
        }

        ChatParticipant c = chatParticipantRepository.findByChatRoomAndMember(chatRoom, member).orElseThrow(() -> new EntityNotFoundException("없다고"));
        chatParticipantRepository.delete(c);

        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByChatRoom(chatRoom);
        if (chatParticipants.isEmpty()) {
            chatRoomRepository.delete(chatRoom);
        }



    }
}
