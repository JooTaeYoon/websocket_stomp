package websocket.project.spring.member.chat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.log.SubSystemLogging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import websocket.project.spring.member.chat.service.ChatService;

import javax.naming.AuthenticationException;

@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final ChatService chatService;

    public StompHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("------ accessor ------ : "+ accessor.getCommand().toString());
        log.info(accessor.getLogin());
        log.info(accessor.getMessage());
        log.info(accessor.getMessageId());
        log.info(accessor.getDestination());
        log.info("------ StompCommand ------ : "+StompCommand.SUBSCRIBE);

        if (StompCommand.CONNECT == accessor.getCommand()) {

            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
//            토큰 검증
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        }

        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("구독좋아요 알림설정 검증!");

            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);

            Claims body = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

            String email = body.getSubject();
            String roomId = accessor.getDestination().split("/")[2];
            log.info("roomId: " + roomId);

            if (!chatService.isRoomParticipant(email, Long.parseLong(roomId))) {
                try {
                    throw new AuthenticationException("해당 룸에 권한이 없습니다.");
                } catch (AuthenticationException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        return message;
    }

}
