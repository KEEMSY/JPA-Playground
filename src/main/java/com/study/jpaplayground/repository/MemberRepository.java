package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.Member;
import com.study.jpaplayground.exception.NoUserException;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/*
EntityManager 를 사용하여 엔터티 단위로 CRUD 처리한다.
- 변경은 트랜잭션 범위 안에서 실행한다.
- persist: 저장
  이 방법은 새 엔터티를 데이터베이스에 저장하는 데 사용된다.
  임시 인스턴스를 지속적으로 만들지만 식별자 값이 즉시 할당된다는 보장은 없다.
  보통 트랜잭션을 커밋하는 시점에는 식별자 값이 할당된다.

- find: 조회
  기본 키로 엔터티를 검색하는 데 사용된다.
  영속성 컨텍스트에서 엔터티가 발견되면 직접 반환된다.
  영속성 컨텍스트에서 엔터티를 찾지 못한 경우에는 데이터베이스에서 조회를 시도하며, 그래도 찾지 못하면 null을 반환한다.

- remove: 삭제
  데이터베이스에서 엔터티를 삭제하는 데 사용된다.
  이 작업이 성공하려면 엔터티가 현재 지속성 컨텍스트에서 관리되어야한다.(조회 시, 존재해야 한다.)

- merge: 수정
  엔터티를 업데이트하는 데 사용된다.
  영속성 컨텍스트에 존재하지 않는 엔터티의 경우 새로운 엔터티를 만들고 그 값을 복사하여 반환한다.
  따라서 merge를 호출한 후에 반환된 엔터티를 사용해야 합니다.

- flush: 영속성 컨텍스트의 변경 내용을 데이터베이스에 반영
  지속성 컨텍스트를 데이터베이스에 동기화한다.
  지속성 컨텍스트에서 관리되는 엔터티에 대한 변경 사항을 데이터베이스에 적용하는 데 사용된다.

- clear: 영속성 컨텍스트 초기화
  영속성 컨텍스트를 효과적으로 지워서 모든 관리되는 엔터티를 분리(detach)합니다.
  일괄 처리에서 메모리를 해제하는 데 유용하다.

- detach: 영속성 컨텍스트에서 해당 엔터티를 분리
  영속성 컨텍스트에서 엔터티를 제거하므로 엔터티에 대한 변경 사항은 지속성 컨텍스트에
  다시 병합되지 않는 한 데이터베이스와 동기화되지 않는다.

- refresh: 데이터베이스에서 엔터티를 다시 조회
  데이터베이스에서 항목 상태를 업데이트하고 항목에 대한 변경 사항이 있는 경우 이를 덮어쓴다.
  엔터티가 현재 데이터베이스 상태를 반영하는지 확인하려는 경우 유용합니다.

- lock: 엔터티에 락을 건다
  주로 동시성 제어 목적으로 특정 잠금 모드에서 엔터티를 잠그는 데 사용된다.


지속:
찾기:
제거:
병합:
플러시:
clear:
분리:
새로고침:
잠금:
 */
@Repository
public class MemberRepository {

    @PersistenceUnit
    private EntityManagerFactory emf;

    public void createMember(String email, String name) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            Member member = new Member(email, name, LocalDateTime.now());
            em.persist(member);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    // 트랜잭션 범위 내에서 변경된 값을 자동 반영한다.
    public void updateMember(String email, String name) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            String jpql = "SELECT m FROM Member m WHERE m.email = :email";
            TypedQuery<Member> query = em.createQuery(jpql, Member.class);
            query.setParameter("email", email);

            Member member;
            try {
                member = query.getSingleResult();
            } catch (NoResultException e) {
                transaction.rollback();
                throw new NoUserException("해당 유저가 존재하지 않습니다.");
            }

            member.changeName(name);
            transaction.commit();

        } catch (Exception | NoUserException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("An error occurred while deleting the member", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // find 로 읽어온 객체 를 전달 해야만 삭제가 됨
    // 만약 find 로 찾아오지 않은 객체를 삭제하려고 하면, 예외가 발생한다.
    // 또는 트랜잭션 범위 밖에서 삭제를 시도하면 예외가 발생한다.
    public void deleteMemberByEmail(String email) throws NoUserException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            String jpql = "SELECT m FROM Member m WHERE m.email = :email";
            TypedQuery<Member> query = em.createQuery(jpql, Member.class);
            query.setParameter("email", email);

            Member member;
            try {
                member = query.getSingleResult();
            } catch (NoResultException e) {
                transaction.rollback();
                throw new NoUserException("해당 유저가 존재하지 않습니다.");
            }

            em.remove(member);
            // 이 시점에 다른 프로세스가 데이터를 삭제하면 예외 발상 -> 이에 따른 알맞은 예외를 처리해주면 됨
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("An error occurred while deleting the member", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // 존재하지 않을 경우, null 을 반환하기 때문에 Null 처리를 해주는 것 이 좋다.
    public Member findMemberById(Long id) throws NoUserException {
        EntityManager em = emf.createEntityManager();

        try {
            Member member = em.find(Member.class, id);
            if (member == null) {
                throw new NoUserException("해당 유저가 존재하지 않습니다.");
            }

            return member;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public Member findMemberByEmail(String email) throws NoUserException {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT m FROM Member m WHERE m.email = :email";
            TypedQuery<Member> query = em.createQuery(jpql, Member.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoUserException("해당 유저가 존재하지 않습니다.");
        }
    }
}

