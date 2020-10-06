package com.example.spring.controller.v1;

import com.example.spring.advice.exception.UserNotFoundException;
import com.example.spring.entity.User;
import com.example.spring.model.response.CommonResult;
import com.example.spring.model.response.ListResult;
import com.example.spring.model.response.SingleResult;
import com.example.spring.repository.UserRepository;
import com.example.spring.service.ResponseService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Api(tags = {"2. User"})
@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseService responseService;

    @ApiImplicitParams(
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access_token",required = true,dataType = "String", paramType = "header")
    )
    @ApiOperation(value = "회원 리스트 조회",notes = "모든 회원을 조회한다.")
    @GetMapping("/users")
    public ListResult<User> findAllUser(){
        return responseService.getListResult(userRepository.findAll());
    }

    @ApiImplicitParams(
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access_token",required = true,dataType = "String", paramType = "header")
    )
    @ApiOperation(value = "회원 단건 조회",notes = "userId로 회원을 조회한다.")
    @GetMapping("/user")
    public SingleResult<User> findUserById(@ApiParam(value = "언어",defaultValue = "ko") @RequestParam String lang){
        // SecurityContext 에서 인증받은 회원의 정보를 얻어온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        return responseService.getSingleResult(userRepository.findByUid(id).orElseThrow(UserNotFoundException::new));
    }

    @ApiImplicitParams(
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access_token",required = true,dataType = "String", paramType = "header")
    )
    @ApiOperation(value = "회원 수정",notes = "회원 정보를 수정한다.")
    @PutMapping("/user")
    public SingleResult<User> modify(@ApiParam(value = "회원번호",required = true)@RequestParam Long id,
                                     @ApiParam(value = "회원이름",required = true) @RequestParam String name){
        User user = User.builder()
                .id(id)
                .name(name)
                .build();

        return responseService.getSingleResult(userRepository.save(user));
    }

    @ApiImplicitParams(
            @ApiImplicitParam(name = "X-AUTH-TOKEN",value = "로그인 성공후 access_token",required = true,dataType = "String", paramType = "header")
    )
    @ApiOperation(value = "회원 삭제",notes = "userId로 회원정보를 삭제한다.")
    @DeleteMapping("/user/{id}")
    public CommonResult delete(@ApiParam(value = "회원번호",required = true)@PathVariable Long id){
        userRepository.deleteById(id);
        return responseService.getSuccessResult();
    }
}
