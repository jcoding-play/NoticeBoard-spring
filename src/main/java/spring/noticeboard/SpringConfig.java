package spring.noticeboard;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.noticeboard.repository.article.ArticleRepository;
import spring.noticeboard.repository.article.JdbcArticleRepository;
import spring.noticeboard.repository.article.JdbcTemplateArticleRepository;
import spring.noticeboard.repository.member.JdbcMemberRepository;
import spring.noticeboard.repository.member.JdbcTemplateMemberRepository;
import spring.noticeboard.repository.member.MemberRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final DataSource dataSource;

    @Bean
    public MemberRepository memberRepository() {
        return new JdbcTemplateMemberRepository(dataSource);
    }

    @Bean
    public ArticleRepository articleRepository() {
        return new JdbcTemplateArticleRepository(dataSource);
    }
}
