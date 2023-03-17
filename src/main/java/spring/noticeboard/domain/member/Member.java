package spring.noticeboard.domain.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Member {

    private Long id;

    @NotBlank
    private String username;
    @NotBlank
    private String loginId;
    @NotBlank
    private String password;

    public Member(String username, String loginId, String password) {
        this.username = username;
        this.loginId = loginId;
        this.password = password;
    }
}
