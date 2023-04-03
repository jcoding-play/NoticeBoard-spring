package spring.noticeboard.repository.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import spring.noticeboard.domain.member.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JdbcMemberRepository implements MemberRepository {

    private final DataSource dataSource;

    @Override
    public Member save(Member member) {
        String sql = "insert into member(username, login_id, password) values(?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getUsername());
            pstmt.setString(2, member.getLoginId());
            pstmt.setString(3, member.getPassword());

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new NoSuchElementException("id 조회 실패");
            }

            log.info("saved member={}", member);
            return member;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = "select * from member where id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String loginId = rs.getString("login_id");
                String password = rs.getString("password");
                Member member = new Member(username, loginId, password);
                member.setId(id);

                return Optional.of(member);
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
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, loginId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String login_id = rs.getString("login_id");
                String password = rs.getString("password");
                Member member = new Member(username, login_id, password);
                member.setId(id);

                return Optional.of(member);
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
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> memberList = new ArrayList<>();
            while (rs.next()) {
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String login_id = rs.getString("login_id");
                String password = rs.getString("password");
                Member member = new Member(username, login_id, password);
                member.setId(id);

                memberList.add(member);
            }
            return memberList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
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
