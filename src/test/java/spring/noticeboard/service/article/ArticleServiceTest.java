package spring.noticeboard.service.article;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import spring.noticeboard.domain.article.Article;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.file.FileStore;
import spring.noticeboard.repository.article.MemoryArticleRepository;
import spring.noticeboard.repository.member.MemoryMemberRepository;
import spring.noticeboard.service.member.MemberService;
import spring.noticeboard.web.SessionConst;
import spring.noticeboard.web.article.form.ArticleSaveForm;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class ArticleServiceTest {

    ArticleService articleService;
    MemberService memberService;
    FileStore fileStore;
    MemoryMemberRepository memberRepository;
    MemoryArticleRepository articleRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        articleRepository = new MemoryArticleRepository();
        fileStore = new FileStore();
        memberService = new MemberService(memberRepository);
        articleService = new ArticleService(articleRepository, memberService, fileStore);
    }

    @AfterEach
    void afterEach() {
        articleRepository.reset();
    }

    @Test
    void save() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = new Member("member1", "member", "member");
        memberService.join(member);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID, member.getId());

        ArticleSaveForm form = new ArticleSaveForm("test", "test", "test", null);
        Article savedArticle = articleService.save(form, request);

        assertThat(savedArticle.getCreateBy()).isEqualTo(member.getLoginId());
    }
}