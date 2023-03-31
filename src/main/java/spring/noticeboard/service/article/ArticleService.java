package spring.noticeboard.service.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.noticeboard.domain.article.Article;
import spring.noticeboard.domain.article.UploadFile;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.file.FileStore;
import spring.noticeboard.repository.article.ArticleRepository;
import spring.noticeboard.service.member.MemberService;
import spring.noticeboard.web.SessionConst;
import spring.noticeboard.web.article.form.ArticleSaveForm;
import spring.noticeboard.web.article.form.ArticleUpdateForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberService memberService;
    private final FileStore fileStore;

    public Article save(ArticleSaveForm form, HttpServletRequest request) throws IOException {
        String createBy = getLoginId(request);
        String title = form.getTitle();
        String author = form.getAuthor();
        String content = form.getContent();
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        LocalDateTime localDateTime = LocalDateTime.now();

        Article article = new Article(createBy, title, author, content, attachFile, localDateTime);
        articleRepository.save(article);
        return article;
    }

    public Optional<Article> findArticle(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findArticles() {
        return articleRepository.findAll();
    }

    public void update(Long id, ArticleUpdateForm form) {

        findArticle(id)
                .ifPresent(article -> {
                    article.setTitle(form.getTitle());
                    article.setAuthor(form.getAuthor());
                    article.setContent(form.getContent());
                    try {
                        article.setAttachFile(fileStore.storeFile(form.getAttachFile()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    article.setLocalDateTime(LocalDateTime.now());
                });
    }

    public void delete(Long id) {
        articleRepository.delete(id);
    }

    private String getLoginId(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Long memberId = (Long) session.getAttribute(SessionConst.LOGIN_MEMBER_ID);

        Member member = memberService.findOne(memberId).orElse(null);
        if (member == null) {
            return null;
        }

        return member.getLoginId();
    }
}
