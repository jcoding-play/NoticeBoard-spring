package spring.noticeboard;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.noticeboard.repository.article.ArticleRepository;
import spring.noticeboard.repository.article.JdbcArticleRepository;
import spring.noticeboard.repository.article.JdbcTemplateArticleRepository;
import spring.noticeboard.repository.article.JpaArticleRepository;
import spring.noticeboard.repository.member.JdbcMemberRepository;
import spring.noticeboard.repository.member.JdbcTemplateMemberRepository;
import spring.noticeboard.repository.member.JpaMemberRepository;
import spring.noticeboard.repository.member.MemberRepository;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

//    private final DataSource dataSource;
    private final EntityManager em;

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public ArticleRepository articleRepository() {
        return new JpaArticleRepository(em);
    }
}
