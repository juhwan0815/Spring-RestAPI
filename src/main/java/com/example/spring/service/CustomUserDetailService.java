package com.example.spring.service;

import com.example.spring.advice.exception.UserNotFoundException;
import com.example.spring.common.CacheKey;
import com.example.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Cacheable(value = CacheKey.USER, key = "#userPk", unless="#result == null") // 해당 메서드가 호출 될 때 캐시가 없으면 DB에서 가져와 캐시를 생성하고 데이터를 반환
    @Override                                                                    // 캐시가 이미 있는 경우 DB를 거치지 않고 바로 캐시 데이터를 반환한다.
    public UserDetails loadUserByUsername(String userPk)  {                      // value 에는 저장시 키 값, key에는 키 생성시 추가로 덧붙일 파라미터의 정보를 선언
        return userRepository.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new); // 캐시키는 user::1004와 같은 형태로 생성
    }                                                                                                 // unless는 메서드 결과가 null 이 아닌 경우에만 캐싱
}
