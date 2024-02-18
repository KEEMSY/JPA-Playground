package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.Member;
import com.study.jpaplayground.exception.NoUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    void createUser() throws NoUserException {
        // given
        String email = "user@emailWithAnnotation1";
        String name = "user";

        // when
        memberRepository.createMember(email, name);

        // then
        Member member = memberRepository.findMemberByEmail(email);
        assertEquals(email, member.getEmail());
        assertEquals(name, member.getName());
    }

    @Test
    @DisplayName("유저 이름 변경 테스트")
    void updateUser() throws NoUserException {
        // given
        String email = "user@emailWithAnnotation";
        String name = "user";
        memberRepository.createMember(email, name);

        // when
        String changedName = "changedUser";
        memberRepository.updateMember(email, changedName);

        // then
        Member member = memberRepository.findMemberByEmail(email);
        assertEquals(email, member.getEmail());
        assertEquals(changedName, member.getName());
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void deleteUser() throws NoUserException {
        // given
        String email = "user@emailWithAnnotation";
        String name = "user";
        memberRepository.createMember(email, name);
        Member createdMember = memberRepository.findMemberByEmail(email);

        // when
        memberRepository.deleteMemberByEmail(email);

        // then
        Long memberId = createdMember.getId();
        Member member = memberRepository.findMemberById(memberId);
        assertEquals(null, member);
    }

    @Test
    @DisplayName("존재하지 않는 유저 삭제 테스트")
    void deleteUserWithNoUser() {
        // given
        String email = "user@emailWithAnnotation";

        // when, then
        assertThrows(NoUserException.class, ()
                -> memberRepository.deleteMemberByEmail(email));
    }
}