package websocket.project.spring.member.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import websocket.project.spring.member.chat.domain.ReadStatus;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, Long> {

}
