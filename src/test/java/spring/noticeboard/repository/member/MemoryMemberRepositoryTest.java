package spring.noticeboard.repository.member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.noticeboard.domain.member.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    @AfterEach
    void afterEach() {
        memberRepository.reset();
    }

    @Test
    @DisplayName("회원을 저장할때 자동으로 ID 값을 증가하고 저장해야 합니다.")
    void checkId() {
        Member member1 = new Member("member1", "test1", "test1");
        Member member2 = new Member("member2", "test2", "test2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        assertThat(member1.getId()).isEqualTo(1);
        assertThat(member2.getId()).isEqualTo(2);
    }

    @Test
    void save() {
        Member member = new Member("member", "test1", "test1");
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(member).isEqualTo(findMember);
    }

    @Test
    void findByLoginId() {
        Member member1 = new Member("member1", "test1", "test1");
        Member member2 = new Member("member2", "test2", "test2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember = memberRepository.findByLoginId("test2").get();

        assertThat(findMember).isEqualTo(member2);
    }

    @Test
    void findAll() {
        Member member1 = new Member("member1", "test1", "test1");
        Member member2 = new Member("member2", "test2", "test2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(2);
        assertThat(members).contains(member1, member2);
    }
}