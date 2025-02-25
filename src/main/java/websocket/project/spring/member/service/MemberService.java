package websocket.project.spring.member.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websocket.project.spring.member.domain.Member;
import websocket.project.spring.member.dto.MemberSaveReqDto;
import websocket.project.spring.member.repository.MemberRepository;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(MemberSaveReqDto memberSaveReqDto) throws IllegalAccessException {
//        이미 가입 되어 있는 이메일 검증
        if (memberRepository.findByEmail(memberSaveReqDto.getEmail()).isPresent()) {
            throw new IllegalAccessException("이미 존재합니다.");
        }

        Member newMember = Member.builder().name(memberSaveReqDto.getName()).email(memberSaveReqDto.getEmail()).password(memberSaveReqDto.getPassword()).build();
        Member member = memberRepository.save(newMember);

        return member;
    }


}
