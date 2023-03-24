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
import spring.noticeboard.web.article.form.ArticleSaveForm;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

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
    public String article(@PathVariable Long id, Model model) {

        articleService.findArticle(id)
                .ifPresent(article -> model.addAttribute("article", article));

        return "article/article";
    }
}
