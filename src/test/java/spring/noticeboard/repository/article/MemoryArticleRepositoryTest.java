package spring.noticeboard.repository.article;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import spring.noticeboard.domain.article.Article;
import spring.noticeboard.file.FileStore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryArticleRepositoryTest {

    FileStore fileStore = new FileStore();
    MemoryArticleRepository articleRepository = new MemoryArticleRepository(fileStore);

    @AfterEach
    void afterEach() {
        articleRepository.reset();
    }

    @Test
    void save() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Article article = new Article("tester", "test", "test", "test", null, null, format);
        articleRepository.save(article);

        Article findArticle = articleRepository.findById(article.getId()).get();

        assertThat(article).isEqualTo(findArticle);
    }

    @Test
    void findAll() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Article article1 = new Article("tester1", "test1", "test1", "test1", null, null, format);
        articleRepository.save(article1);
        Article article2 = new Article("tester2", "test2", "test2", "test2", null, null, format);
        articleRepository.save(article2);

        List<Article> result = articleRepository.findAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(article1, article2);
    }

    @Test
    void update() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Article article = new Article("tester", "test", "test", "test", null, null, format);
        articleRepository.save(article);
        Long articleId = article.getId();

        format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Article updateForm = new Article("update", "update", "update", "update", "update", "update", format);
        articleRepository.update(articleId, updateForm);
        Article findArticle = articleRepository.findById(articleId).get();

        assertThat(article).isEqualTo(findArticle);
        assertThat(findArticle.getTitle()).isEqualTo("update");
        assertThat(findArticle.getAuthor()).isEqualTo("update");
    }

    @Test
    void delete() {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Article article = new Article("tester", "test", "test", "test", null, null, format);
        articleRepository.save(article);

        articleRepository.delete(article.getId());

        assertThat(articleRepository.findAll().size()).isEqualTo(0);
        assertThat(articleRepository.findById(article.getId()).orElse(null)).isNull();
    }
}