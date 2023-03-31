package spring.noticeboard.repository.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import spring.noticeboard.domain.article.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Repository
public class MemoryArticleRepository implements ArticleRepository {

    private static final Map<Long, Article> store = new ConcurrentHashMap<>();
    private static AtomicLong sequence = new AtomicLong(0);

    @Override
    public Article save(Article article) {
        article.setId(sequence.incrementAndGet());
        store.put(article.getId(), article);
        log.info("saved Article={}", article);
        return article;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void update(Long id, Article updateForm) {
        findById(id).ifPresent(article -> {
            article.setTitle(updateForm.getTitle());
            article.setAuthor(updateForm.getAuthor());
            article.setContent(updateForm.getContent());
            article.setAttachFile(updateForm.getAttachFile());
        });
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }

    /**
     * 테스트 용도
     */
    public void reset() {
        sequence.set(0);
        store.clear();
    }
}
