package spring.noticeboard.domain.article;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Article {

    private Long id;

    private String createBy;
    private String title;
    private String author;
    private String content;
    private List<UploadFile> uploadFiles;
    private LocalDateTime localDateTime;

    public Article(String createBy, String title, String author, String content, List<UploadFile> uploadFiles, LocalDateTime localDateTime) {
        this.createBy = createBy;
        this.title = title;
        this.author = author;
        this.content = content;
        this.uploadFiles = uploadFiles;
        this.localDateTime = localDateTime;
    }
}
