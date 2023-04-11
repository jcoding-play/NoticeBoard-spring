package spring.noticeboard.domain.article;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Article {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_by")
    private String createBy;
    private String title;
    private String author;
    private String content;
    @Column(name = "upload_filename")
    private String uploadFilename;
    @Column(name = "store_filename")
    private String storeFilename;
    @Column(name = "local_date_time")
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
