package spring.noticeboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spring.noticeboard.domain.article.Article;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.repository.article.ArticleRepository;
import spring.noticeboard.repository.member.MemberRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

//@Component
@RequiredArgsConstructor
public class TestData {
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        Article article1 = new Article(null, "title1", "author1", "content1", null, LocalDateTime.now());
        Article article2 = new Article(null, "title2", "author2", "content2", null, LocalDateTime.now());
        articleRepository.save(article1);
        articleRepository.save(article2);

        Member member1 = new Member("test1", "test", "test");
        Member member2 = new Member("test2", "test2", "test2");
        memberRepository.save(member1);
        memberRepository.save(member2);
    }
}