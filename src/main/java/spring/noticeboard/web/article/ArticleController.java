package spring.noticeboard.web.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spring.noticeboard.domain.article.Article;
import spring.noticeboard.service.article.ArticleService;
import spring.noticeboard.service.member.MemberService;
import spring.noticeboard.web.SessionConst;
import spring.noticeboard.web.article.form.ArticleSaveForm;
import spring.noticeboard.web.article.form.ArticleUpdateForm;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    @GetMapping("/add")
    public String addArticle(@ModelAttribute("form") ArticleSaveForm form) {
        return "article/addArticle";
    }

    @PostMapping("/add")
    public String saveArticle(@Validated @ModelAttribute("form") ArticleSaveForm form, BindingResult bindingResult,
                              HttpServletRequest request, RedirectAttributes redirectAttributes, Model model) throws IOException {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "article/addArticle";
        }

        Article savedArticle = articleService.save(form, request);
        redirectAttributes.addAttribute("id", savedArticle.getId());

        return "redirect:/article/{id}";
    }

    @GetMapping("/{id}")
    public String article(@PathVariable Long id, Model model,
                          @SessionAttribute(name = SessionConst.LOGIN_MEMBER_ID, required = false) Long memberId) {

        articleService.findArticle(id)
                .ifPresent(article -> {
                    model.addAttribute("article", article);
                    memberService.findOne(memberId)
                            .ifPresent(member -> {
                                if (member.getLoginId().equals(article.getCreateBy())) {
                                    model.addAttribute("status", true);
                                }
                            });
                });

        return "article/article";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        articleService.findArticle(id)
                .ifPresent(article -> model.addAttribute("form", article));

        return "article/updateForm";
    }

    @PostMapping("/{id}/edit")
    public String edit(@Validated @ModelAttribute("form") ArticleUpdateForm form, BindingResult bindingResult,
                       @PathVariable Long id, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "article/updateForm";
        }

        articleService.update(id, form);
        redirectAttributes.addAttribute("id", id);

        return "redirect:/article/{id}";
    }
}
