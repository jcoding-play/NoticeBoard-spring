package spring.noticeboard.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.noticeboard.domain.member.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new ConcurrentHashMap<>();
    private static AtomicLong sequence = new AtomicLong(0);

    @Override
    public Member save(Member member) {
        member.setId(sequence.incrementAndGet());
        store.put(member.getId(), member);
        log.info("saved member={}", member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return Optional.ofNullable(store.get(memberId));
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(member -> member.getLoginId().equals(loginId))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    /**
     * 테스트 용도
     */
    public void reset() {
        sequence.set(0);
        store.clear();
    }
}
