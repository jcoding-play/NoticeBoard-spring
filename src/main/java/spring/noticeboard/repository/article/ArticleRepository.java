package spring.noticeboard.repository.article;

import spring.noticeboard.domain.article.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Article save(Article article);

    Optional<Article> findById(Long id);

    List<Article> findAll();

    void update(Long id, Article updateForm);

    void delete(Long id);
}
