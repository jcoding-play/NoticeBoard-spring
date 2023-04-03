package spring.noticeboard;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.noticeboard.repository.member.JdbcMemberRepository;
import spring.noticeboard.repository.member.MemberRepository;
import spring.noticeboard.repository.member.MemoryMemberRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final DataSource dataSource;

    @Bean
    public MemberRepository memberRepository() {
        return new JdbcMemberRepository(dataSource);
    }
}
