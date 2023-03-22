package spring.noticeboard.service.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.repository.member.MemoryMemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    void afterEach() {
        memberRepository.reset();
    }

    @Test
    void join() {
        Member member = new Member("member1", "test1", "test1");
        memberService.join(member);

        Member findMember = memberService.findOne(member.getId()).get();

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("중복 아이디 검증")
    void duplicateMember() {
        Member member1 = new Member("member1", "test1", "test1");
        memberService.join(member1);

        //로그인 아이디가 중복
        Member member2 = new Member("member2", "test1", "test2");
        assertThatThrownBy(() -> memberService.join(member2)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void findMembers() {
        Member member1 = new Member("member1", "test1", "test1");
        Member member2 = new Member("member2", "test2", "test2");
        memberService.join(member1);
        memberService.join(member2);

        List<Member> members = memberService.findMembers();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members).contains(member1, member2);
    }
}