package spring.noticeboard.repository.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import spring.noticeboard.domain.member.Member;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JdbcTemplateMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        String sql = "select * from member where id=:id";

        Map<String, Object> param = Map.of("id", memberId);
        try {
            Member member = template.queryForObject(sql, param, MemberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id=:loginId";

        Map<String, Object> param = Map.of("loginId", loginId);
        try {
            Member member = template.queryForObject(sql, param, MemberRowMapper());
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        return template.query(sql, MemberRowMapper());
    }

    private RowMapper<Member> MemberRowMapper() {
        return (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String username = rs.getString("username");
            String loginId = rs.getString("login_id");
            String password = rs.getString("password");
            Member member = new Member(username, loginId, password);

            member.setId(id);
            return member;
        };
    }
}
