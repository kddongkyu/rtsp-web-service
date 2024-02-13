package data.service;

import data.object.dto.LoginDto;
import data.object.dto.RegisterDto;
import data.object.entity.MemberEntity;
import data.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean login(LoginDto dto, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(60 * 60);

        log.info("MemberService - Login Session ID: " + session.getId());

        MemberEntity entity = memberRepository.findByManagerEmail(dto.getEmail()).orElseThrow();
        boolean result = passwordEncoder.matches(dto.getPassword(), entity.getPassword());

        session.setAttribute("login", true);
        session.setAttribute("memberId", entity.getMemberId());

        System.out.println(session.getAttribute("memberId"));

        return result;
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    public Long register(RegisterDto dto) {
        MemberEntity member = dtoToEntity(dto);
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        memberRepository.save(member);
        member.setIndividualPort((int) (60000 + member.getMemberId()));
        return member.getMemberId();
    }

    private MemberEntity dtoToEntity(RegisterDto dto) {
        return new MemberEntity(dto.getManagerName(), dto.getManagerEmail(), dto.getManagerPhonenumber(), dto.getCompanyName(), dto.getCompanyNumber(), dto.getPassword());
    }

}
