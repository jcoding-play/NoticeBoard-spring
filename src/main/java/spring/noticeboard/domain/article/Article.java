package spring.noticeboard.domain.article;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {

    private Long id;

    private String createBy;
    private String title;
    private String author;
    private String content;
    private UploadFile attachFile;
    private LocalDateTime localDateTime;

    public Article(String createBy, String title, String author, String content, UploadFile attachFile, LocalDateTime localDateTime) {
        this.createBy = createBy;
        this.title = title;
        this.author = author;
        this.content = content;
        this.attachFile = attachFile;
        this.localDateTime = localDateTime;
    }
}
