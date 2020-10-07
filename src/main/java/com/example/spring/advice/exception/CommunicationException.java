package com.example.spring.advice.exception;

public class CommunicationException extends RuntimeException{

    public CommunicationException(String msg,Throwable t){
        super(msg,t);
    }

    public CommunicationException(String msg){
        super(msg);
    }

    public CommunicationException(){
        super();
    }


}
