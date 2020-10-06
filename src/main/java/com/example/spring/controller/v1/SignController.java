package com.example.spring.controller.v1;

import com.example.spring.advice.exception.EmailSigninFailedException;
import com.example.spring.config.security.JwtTokenProvider;
import com.example.spring.entity.User;
import com.example.spring.model.response.CommonResult;
import com.example.spring.model.response.SingleResult;
import com.example.spring.repository.UserRepository;
import com.example.spring.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Api(tags = {"1.Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인",notes = "이메일 회원 로그인을 한다.")
    @PostMapping("/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID: 이메일",required = true) @RequestParam String id,
                                       @ApiParam(value = "비밀번호",required = true) @RequestParam String password){
        User user = userRepository.findByUid(id).orElseThrow(EmailSigninFailedException::new);
        if(!passwordEncoder.matches(password,user.getPassword()))
            throw new EmailSigninFailedException();

        System.out.println("gd");
        return  responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()),user.getRoles()));
    }

    @ApiOperation(value = "가입",notes = "회원가입을 한다.")
    @PostMapping("/signup")
    public CommonResult signin(@ApiParam(value = "회원ID:이메일",required = true)@RequestParam String id,
                               @ApiParam(value = "비밀번호",required = true) @RequestParam String password,
                               @ApiParam(value = "이름",required = true) @RequestParam String name){
        userRepository.save(User.builder()
        .uid(id)
        .password(passwordEncoder.encode(password))
        .name(name)
        .roles(Collections.singletonList("ROLE_USER"))
                .build());
        return responseService.getSuccessResult();
    }
}
