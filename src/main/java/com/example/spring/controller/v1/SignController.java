package com.example.spring.controller.v1;

import com.example.spring.advice.exception.EmailSigninFailedException;
import com.example.spring.advice.exception.UserExistException;
import com.example.spring.advice.exception.UserNotFoundException;
import com.example.spring.config.security.JwtTokenProvider;
import com.example.spring.entity.User;
import com.example.spring.model.response.CommonResult;
import com.example.spring.model.response.SingleResult;
import com.example.spring.model.social.KakaoProfile;
import com.example.spring.repository.UserRepository;
import com.example.spring.service.KakaoService;
import com.example.spring.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.Optional;

@Api(tags = {"1.Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class SignController {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;
    private final KakaoService kakaoService;

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

    @ApiOperation(value = "소설 로그인",notes = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/signin/{provider}")
    public SingleResult<String> signinByProvider(@ApiParam(value = "서비스 제공자 provider",required = true,defaultValue = "kakao") @PathVariable String provider
                                                ,@ApiParam(value = "소셜 access_token",required = true) @RequestParam String accessToken){
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        User user = userRepository.findByUidAndProvider(String.valueOf(profile.getId()),provider).orElseThrow(UserNotFoundException::new);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getId()),user.getRoles()));
    }

    @ApiOperation(value = "소셜계정 가입",notes = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/signup/{provider}")
    public CommonResult signupProvider(@ApiParam(value = "서비스 제공자 provider",required = true,defaultValue = "kakao")@PathVariable String provider,
                                       @ApiParam(value = "소셜access_token",required = true) @RequestParam String accessToken,
                                       @ApiParam(value = "이름",required = true) @RequestParam String name){
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<User> user = userRepository.findByUidAndProvider(String.valueOf(profile.getId()),provider);
        if(user.isPresent()){
            throw new UserExistException();
        }

        userRepository.save(User.builder()
        .uid(String.valueOf(profile.getId()))
        .provider(provider)
        .name(name)
        .roles(Collections.singletonList("ROLE_USER"))
        .build());
        return responseService.getSuccessResult();

    }


}
