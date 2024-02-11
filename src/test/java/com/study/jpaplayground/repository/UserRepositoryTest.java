package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    void createUser() {
        // given
        String email = "user@emailWithAnnotation";
        String name = "user";

        // when
        userRepository.createUser(email, name);

        // then
        User user = userRepository.getUser(email);
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
    }
}