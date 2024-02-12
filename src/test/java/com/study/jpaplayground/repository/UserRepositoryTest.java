package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.User;
import com.study.jpaplayground.exception.NoUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    @DisplayName("유저 이름 변경 테스트")
    void updateUser() {
        // given
        String email = "user@emailWithAnnotation";
        String name = "user";
        userRepository.createUser(email, name);

        // when
        String changedName = "changedUser";
        userRepository.updateUser(email, changedName);

        // then
        User user = userRepository.getUser(email);
        assertEquals(email, user.getEmail());
        assertEquals(changedName, user.getName());
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void deleteUser() throws NoUserException {
        // given
        String email = "user@emailWithAnnotation";
        String name = "user";
        userRepository.createUser(email, name);

        // when
        userRepository.deleteUser(email);

        // then
        User user = userRepository.getUser(email);
        assertEquals(null, user);
    }

    @Test
    @DisplayName("존재하지 않는 유저 삭제 테스트")
    void deleteUserWithNoUser() {
        // given
        String email = "user@emailWithAnnotation";

        // when, then
        assertThrows(NoUserException.class, ()
                -> userRepository.deleteUser(email));
    }
}