package data.controller;

import data.object.dto.LoginDto;
import data.object.dto.RegisterDto;
import data.service.BusService;
import data.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@CrossOrigin
public class memberController {

    private final MemberService memberService;
    private final BusService busService;

    public memberController(MemberService memberService, BusService busService) {
        this.memberService = memberService;
        this.busService = busService;
    }

    @PostMapping("/login")
    private ResponseEntity<Boolean> login(@RequestBody LoginDto memberData, HttpServletRequest request) {
        return new ResponseEntity<>(memberService.login(memberData,request), HttpStatus.OK);
    }

    @GetMapping("/logout")
    private ResponseEntity<Void> logout(HttpServletRequest request) {
        memberService.logout(request);
         return new ResponseEntity<>(HttpStatus.OK);
    }



    @PostMapping
    private ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        return new ResponseEntity<>("성공적으로 가입을 완료했습니다. 가입된 멤버의 ID : " + memberService.register((registerDto)),HttpStatus.OK);
    }

}
