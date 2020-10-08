package com.example.spring.advice.exception;

import org.aspectj.weaver.ast.Not;

public class NotOwnerException extends RuntimeException{

    private static final Long serialVersionUID = 2241549550934267615L;

    public NotOwnerException(String msg,Throwable t){
        super(msg,t);
    }

    public NotOwnerException(String msg){
        super(msg);
    }

    public NotOwnerException(){
        super();
    }
}
