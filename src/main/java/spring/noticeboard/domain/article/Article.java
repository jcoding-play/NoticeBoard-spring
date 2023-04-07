package spring.noticeboard.domain.article;

import lombok.Data;

@Data
public class Article {

    private Long id;

    private String createBy;
    private String title;
    private String author;
    private String content;
    private String uploadFilename;
    private String storeFilename;
    private String localDateTime;

    public Article() {
    }

    public Article(String createBy, String title, String author, String content, String uploadFilename, String storeFilename, String localDateTime) {
        this.createBy = createBy;
        this.title = title;
        this.author = author;
        this.content = content;
        this.uploadFilename = uploadFilename;
        this.storeFilename = storeFilename;
        this.localDateTime = localDateTime;
    }
}
