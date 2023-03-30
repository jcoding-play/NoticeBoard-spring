package spring.noticeboard.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import spring.noticeboard.domain.article.Article;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.repository.article.ArticleRepository;
import spring.noticeboard.repository.member.MemberRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID, required = false) Long memberId, Model model) {

        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);

        if (memberId == null) {
            return "home";
        }

        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            return "home";
        } else {
            model.addAttribute("member", member);
            return "loginHome";
        }
    }
}
