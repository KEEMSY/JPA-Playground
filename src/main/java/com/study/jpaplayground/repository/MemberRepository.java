package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.Member;
import com.study.jpaplayground.exception.NoUserException;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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

