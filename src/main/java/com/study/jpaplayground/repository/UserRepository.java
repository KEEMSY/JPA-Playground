package com.study.jpaplayground.repository;

import com.study.jpaplayground.JPA.entity.User;
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

