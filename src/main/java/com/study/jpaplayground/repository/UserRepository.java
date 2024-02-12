package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.User;
import com.study.jpaplayground.exception.NoUserException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class UserRepository {

    @PersistenceUnit
    private EntityManagerFactory emf;

    public void createUser(String email, String name) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User user = new User(email, name, LocalDateTime.now());
            em.persist(user);

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
    public void updateUser(String email, String name) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User user = em.find(User.class, email);
            user.changeName(name);

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

    public void deleteUser(String email) throws NoUserException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User user = em.find(User.class, email);
            if (user == null) {
                throw new NoUserException("해당 유저가 존재하지 않습니다.");
            }
            em.remove(user);

            transaction.commit();
        } catch (Exception | NoUserException e) {
            if (transaction.isActive()) {
                transaction.rollback();
                throw e;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public User getUser(String email) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(User.class, email);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

