package websocket.project.spring.member.chat.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import websocket.project.spring.member.common.domain.BaseTimeEntity;
import websocket.project.spring.member.domain.Member;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReadStatus extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;


    @Column(nullable = false)
    private Boolean isRead;



}
