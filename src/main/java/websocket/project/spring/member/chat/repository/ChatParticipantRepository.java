package websocket.project.spring.member.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import websocket.project.spring.member.chat.domain.ChatParticipant;
import websocket.project.spring.member.chat.domain.ChatRoom;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    List<ChatParticipant> findByChatRoom(ChatRoom chatRoom);


}
