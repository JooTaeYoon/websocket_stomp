package websocket.project.spring.member.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import websocket.project.spring.member.chat.domain.ChatRoom;
import websocket.project.spring.member.chat.domain.ReadStatus;
import websocket.project.spring.member.domain.Member;

import java.util.List;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

    List<ReadStatus> findByChatRoomAndMember(ChatRoom chatRoom, Member member);

    Long countByChatRoomAndMemberAndIsReadFalse(ChatRoom chatRoom, Member member);
}
