package spring.noticeboard.repository.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import spring.noticeboard.domain.article.Article;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JdbcTemplateArticleRepository implements ArticleRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateArticleRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("article")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Article save(Article article) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(article);
        Number key = jdbcInsert.executeAndReturnKey(param);
        article.setId(key.longValue());
        return article;
    }

    @Override
    public Optional<Article> findById(Long id) {
        String sql = "select * from article where id=:id";

        Map<String, Object> param = Map.of("id", id);
        try {
            Article article = template.queryForObject(sql, param, ArticleRowMapper());
            return Optional.of(article);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Article> findAll() {
        String sql = "select * from article";

        return template.query(sql, ArticleRowMapper());
    }

    @Override
    public void update(Long id, Article updateForm) {
        String sql = "update article " +
                "set title=:title, author=:author, content=:content, upload_filename=:uploadFilename, store_filename=:storeFilename, local_date_time=:localDateTime " +
                "where id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", updateForm.getTitle())
                .addValue("author", updateForm.getAuthor())
                .addValue("content", updateForm.getContent())
                .addValue("uploadFilename", updateForm.getUploadFilename())
                .addValue("storeFilename", updateForm.getStoreFilename())
                .addValue("localDateTime", updateForm.getLocalDateTime())
                .addValue("id", id);
        template.update(sql, param);
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from article where id=:id";

        Map<String, Object> param = Map.of("id", id);
        template.update(sql, param);
    }

    private RowMapper<Article> ArticleRowMapper() {
        return BeanPropertyRowMapper.newInstance(Article.class);
    }
}
