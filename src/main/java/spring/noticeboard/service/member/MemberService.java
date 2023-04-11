package spring.noticeboard.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.repository.member.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member member) {
        validateDuplicateMember(member); //중복 아이디 검증
        memberRepository.save(member);
        return member;
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByLoginId(member.getLoginId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다. 입력한 아이디=" + m.getLoginId());
                });
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
}
