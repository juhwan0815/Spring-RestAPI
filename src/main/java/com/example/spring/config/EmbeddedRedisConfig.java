package com.example.spring.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct // 객체의 초기화 부분 was가 띄워질때 실행
    public void redisServer(){
        redisServer = new RedisServer(redisPort);
        redisServer.start();
    }

    @PreDestroy // 마지막 소멸 단계 스프링 컨테이너에서 빈을 제거하기 전에 해야할 작업을 실행
    public void stopRedis(){
        if(redisServer != null){
            redisServer.stop();
        }
    }
}
