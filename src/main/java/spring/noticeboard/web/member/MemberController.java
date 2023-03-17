package spring.noticeboard.web.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.noticeboard.domain.member.Member;
import spring.noticeboard.service.member.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member) {
        return "members/addForm";
    }

    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "members/addForm";
        }

        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            bindingResult.rejectValue("loginId", "duplicate", e.getMessage());
            log.info("errors={}", bindingResult);
            return "members/addForm";
        }

        return "redirect:/";
    }
}
