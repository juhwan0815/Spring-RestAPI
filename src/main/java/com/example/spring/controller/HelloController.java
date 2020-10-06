package com.example.spring.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/helloworld/string")
    @ResponseBody
    public String helloworldString(){
        return "helloworld";
    }

    @GetMapping("/helloworld/json")
    @ResponseBody
    public Hello helloworldJson(){
        Hello hello = new Hello();
        hello.message = "helloworld";
        return hello;
    }

    @GetMapping("/helloworld/page")
    public String helloworld(){
        return "helloworld";
    }

    @Setter
    @Getter
    public static class Hello{
        private String message;
    }
}
