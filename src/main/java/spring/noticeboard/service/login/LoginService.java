package spring.noticeboard.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
