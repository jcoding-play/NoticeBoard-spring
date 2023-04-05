package spring.noticeboard.repository.article;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import spring.noticeboard.domain.article.Article;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class JdbcArticleRepository implements ArticleRepository {

    private final DataSource dataSource;

    @Override
    public Article save(Article article) {
        String sql = "insert into article(create_by, title, author, content, upload_filename, store_filename, local_date_time) " +
                "values(?,?,?,?,?,?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, article.getCreateBy());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getAuthor());
            pstmt.setString(4, article.getContent());
            pstmt.setString(5, article.getUploadFilename());
            pstmt.setString(6, article.getStoreFilename());
            pstmt.setString(7, article.getLocalDateTime());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                article.setId(rs.getLong(1));
            } else {
                throw new NoSuchElementException("id 조회 실패");
            }
            return article;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        String sql = "select * from article where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Long articleId = rs.getLong("id");
                String createBy = rs.getString("create_by");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String content = rs.getString("content");
                String uploadFilename = rs.getString("upload_filename");
                String storeFilename = rs.getString("store_filename");
                String localDateTime = rs.getString("local_date_time");
                Article article = new Article(createBy, title, author, content, uploadFilename, storeFilename, localDateTime);
                article.setId(articleId);

                return Optional.of(article);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<Article> findAll() {
        String sql = "select * from article";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Article> articles = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String createBy = rs.getString("create_by");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String content = rs.getString("content");
                String uploadFilename = rs.getString("upload_filename");
                String storeFilename = rs.getString("store_filename");
                String localDateTime = rs.getString("local_date_time");
                Article article = new Article(createBy, title, author, content, uploadFilename, storeFilename, localDateTime);
                article.setId(id);

                articles.add(article);
            }
            return articles;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void update(Long id, Article updateForm) {
        String sql = "update article " +
                "set title=?, author=?, content=?, upload_filename=?, store_filename=?, local_date_time=? " +
                "where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, updateForm.getTitle());
            pstmt.setString(2, updateForm.getAuthor());
            pstmt.setString(3, updateForm.getContent());
            pstmt.setString(4, updateForm.getUploadFilename());
            pstmt.setString(5, updateForm.getStoreFilename());
            pstmt.setString(6, updateForm.getLocalDateTime());
            pstmt.setLong(7, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from article where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }
}
