package websocket.project.spring.member.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import websocket.project.spring.member.controller.MemberController;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    public StompWebSocketConfig(StompHandler stompHandler) {
        this.stompHandler = stompHandler;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").setAllowedOrigins("http://localhost:3000")
//                http:// 프로토콜도 사용 하게 할 수 있게 해주는 SockJS 라이브러리를 허용해주는 설정
                .withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        /publish/1 형태로 메세지 발행해야 함을 설정
//        /publish로 시작하는 url패턴으로 메시지가 발행되면 @Controller 객체의 @MessageMapping메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/publish");

//        /topic/1 형태로 메세지를 수신(subscribe)해야 함을 설정
//        registry.enableStompBrokerRelay("/topic");
        registry.enableSimpleBroker("/topic");

    }


//    웹소켓 요청(connect, subscribe, disconnect)등의 요청시에는 http header등 http 메세지를 넣어 올수 있고,
//    이를 interceptor인터셉터를 통해 가로채 토큰 등을 검증
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        System.out.println("인터셉터 before");
        registration.interceptors(stompHandler);
        System.out.println("인터셉터 after");
    }
}
