package com.example.spring.repository;

import com.example.spring.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void whenFindByUid_thenReturnUser(){
        String uid = "angrydaddy@gmail.com";
        String name = "angrydaddy";

        userRepository.save(User.builder()
        .uid(uid)
        .password(passwordEncoder.encode("1234"))
        .name(name)
        .roles(Collections.singletonList("ROLE_USER"))
        .build());

        Optional<User> user = userRepository.findByUid(uid);

        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(user.get().getName(),name);
        assertThat(user.get().getName()).isEqualTo(name);

    }
}