package websocket.project.spring.member.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websocket.project.spring.member.common.configs.SecurityConfigs;
import websocket.project.spring.member.domain.Member;
import websocket.project.spring.member.dto.MemberListResDto;
import websocket.project.spring.member.dto.MemberLoginReqDto;
import websocket.project.spring.member.dto.MemberSaveReqDto;
import websocket.project.spring.member.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(MemberSaveReqDto memberSaveReqDto) throws IllegalAccessException {
//        이미 가입 되어 있는 이메일 검증
        if (memberRepository.findByEmail(memberSaveReqDto.getEmail()).isPresent()) {
            throw new IllegalAccessException("이미 존재합니다.");
        }

        Member newMember = Member.builder().name(memberSaveReqDto.getName())
                .email(memberSaveReqDto.getEmail())
                .password(passwordEncoder.encode(memberSaveReqDto.getPassword()))
                .build();
        Member member = memberRepository.save(newMember);

        return member;
    }


    public Member login(MemberLoginReqDto memberLoginReqDto) throws IllegalAccessException {
        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일email."));

        if (!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())) {
            throw new IllegalAccessException("비밀번호가 틀렸습니다");
        }
        return member;
    }

    public List<MemberListResDto> findAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberListResDto> memberListResDtos = new ArrayList<>();
        for (Member m : members) {
            MemberListResDto memberListResDto = new MemberListResDto();
            memberListResDto.setId(m.getId());
            memberListResDto.setName(m.getName());
            memberListResDto.setEmail(m.getEmail());
            memberListResDtos.add(memberListResDto);
        }
        return memberListResDtos;
    }

}
