package com.example.spring.common;
// 캐시 세팅 시 사용할 key와 expire 등 정적 정보를 하나의 class에서 정의
public class CacheKey {

    public static final int DEFAULT_EXPIRE_SEC = 60; // 1분

    public static final String USER = "user";

    public static final int USER_EXPIRE_SEC = 60 * 5; // 5분

    public static final String BOARD = "board";

    public static final int BOARD_EXPIRE_SEC = 60 * 10; // 10분

    public static final String POST = "post";

    public static final String POSTS = "posts";

    public static final int POST_EXPIRE_SEC = 60 * 5; // 5분
}
