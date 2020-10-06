package com.example.spring.service;

import com.example.spring.advice.exception.UserNotFoundException;
import com.example.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk)  {
        return userRepository.findById(Long.valueOf(userPk)).orElseThrow(UserNotFoundException::new);
    }
}
