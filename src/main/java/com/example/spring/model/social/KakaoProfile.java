package com.example.spring.model.social;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class KakaoProfile {

    private Long id;
    private Properties properties;

    @Getter
    @Setter
    @ToString
    private static class Properties{
        private String nickName;
        private String thumbnail_image;
        private String profile_image;
    }
}
