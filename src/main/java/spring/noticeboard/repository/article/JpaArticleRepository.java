package spring.noticeboard.repository.article;

import spring.noticeboard.domain.article.Article;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaArticleRepository implements ArticleRepository {

    private final EntityManager em;

    public JpaArticleRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Article save(Article article) {
        em.persist(article);
        return article;
    }

    @Override
    public Optional<Article> findById(Long id) {
        Article article = em.find(Article.class, id);
        return Optional.ofNullable(article);
    }

    @Override
    public List<Article> findAll() {
        String jpql = "select a from Article a";

        return em.createQuery(jpql, Article.class).getResultList();
    }

    @Override
    public void update(Long id, Article updateForm) {
        Article article = em.find(Article.class, id);
        article.setTitle(updateForm.getTitle());
        article.setAuthor(updateForm.getAuthor());
        article.setContent(updateForm.getContent());
        article.setUploadFilename(updateForm.getUploadFilename());
        article.setStoreFilename(updateForm.getStoreFilename());
        article.setLocalDateTime(updateForm.getLocalDateTime());
    }

    @Override
    public void delete(Long id) {
        Article article = em.find(Article.class, id);
        em.remove(article);
    }
}
