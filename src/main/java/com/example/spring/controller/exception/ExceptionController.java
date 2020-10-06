package com.example.spring.controller.exception;

import com.example.spring.advice.exception.AuthenticationEntryPointException;
import com.example.spring.model.response.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entrypoint")
    public CommonResult entrypointException(){
        throw new AuthenticationEntryPointException();
    }

    @GetMapping("/accessdenied")
    public CommonResult accessdeniedException(){
        throw new AccessDeniedException("");
    }
}
