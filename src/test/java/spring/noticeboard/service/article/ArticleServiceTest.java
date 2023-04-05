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
import spring.noticeboard.web.article.form.ArticleUpdateForm;

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
        articleRepository = new MemoryArticleRepository(fileStore);
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

    @Test
    void update() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = new Member("member1", "member", "member");
        memberService.join(member);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID, member.getId());

        ArticleSaveForm form = new ArticleSaveForm("test", "test", "test", null);
        Article savedArticle = articleService.save(form, request);

        ArticleUpdateForm updateForm = new ArticleUpdateForm("update", "update", "update", null);
        articleService.update(savedArticle.getId(), updateForm);

        assertThat(savedArticle.getTitle()).isEqualTo("update");
        assertThat(savedArticle.getAuthor()).isEqualTo("update");
        assertThat(savedArticle.getContent()).isEqualTo("update");
    }

    @Test
    void getSessionId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = new Member("memberA", "qwer", "qwer!");
        memberService.join(member);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID, member.getId());

        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);
        Member findMember = memberService.findOne(memberId).get();

        assertThat(findMember.getLoginId()).isEqualTo("qwer");
    }

    @Test
    void delete() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = new Member("member1", "member", "member");
        memberService.join(member);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID, member.getId());

        ArticleSaveForm form1 = new ArticleSaveForm("test1", "test1", "test1", null);
        Article savedArticle1 = articleService.save(form1, request);
        ArticleSaveForm form2 = new ArticleSaveForm("test2", "test2", "test2", null);
        Article savedArticle2 = articleService.save(form2, request);

        assertThat(articleService.findArticles().size()).isEqualTo(2);
        articleService.delete(savedArticle1.getId());
        assertThat(articleService.findArticles().size()).isEqualTo(1);
        articleService.delete(savedArticle2.getId());
        assertThat(articleService.findArticles().size()).isEqualTo(0);
    }
}