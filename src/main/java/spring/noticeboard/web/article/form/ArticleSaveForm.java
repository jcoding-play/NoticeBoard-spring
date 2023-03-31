package spring.noticeboard.web.article.form;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
public class ArticleSaveForm {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String content;
    private MultipartFile attachFile;

    public ArticleSaveForm(String title, String author, String content, MultipartFile attachFile) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.attachFile = attachFile;
    }
}
