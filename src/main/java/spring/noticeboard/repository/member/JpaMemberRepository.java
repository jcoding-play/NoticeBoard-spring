package spring.noticeboard.repository.member;

import spring.noticeboard.domain.member.Member;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        Member member = em.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String jpql = "select m from Member m where login_id=:loginId";

        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        query.setParameter("loginId", loginId);

        Member member = query.getSingleResult();
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll() {
        String jpql = "select m from Member m";

        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        return query.getResultList();
    }
}
