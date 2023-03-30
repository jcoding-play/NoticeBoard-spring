package spring.noticeboard.web.article.form;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
public class ArticleUpdateForm {

    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String content;
    private List<MultipartFile> multipartFiles;

    public ArticleUpdateForm(String title, String author, String content, List<MultipartFile> multipartFiles) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.multipartFiles = multipartFiles;
    }
}
