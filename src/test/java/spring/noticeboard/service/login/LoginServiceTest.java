package spring.noticeboard.service.login;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.repository.member.MemoryMemberRepository;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.*;

class LoginServiceTest {

    MemoryMemberRepository memberRepository = new MemoryMemberRepository();
    LoginService loginService = new LoginService(memberRepository);

    @AfterEach
    void afterEach() {
        memberRepository.reset();
    }

    @Test
    void login() {
        Member member = new Member("member1", "test", "test");
        memberRepository.save(member);

        Member loginMember = loginService.login(member.getLoginId(), member.getPassword());
        Member notFoundMember = loginService.login("loginFail", "loginFail");

        assertThat(loginMember).isEqualTo(member);
        assertThat(notFoundMember).isNull();
    }

    @Test
    void logout() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = new Member("member1", "test", "test");
        memberRepository.save(member);

        loginService.login(member.getLoginId(), member.getPassword());
        HttpSession session = request.getSession();
        assertThat(session).isNotNull();

        loginService.logout(request);
        session = request.getSession(false);
        assertThat(session).isNull();
    }
}