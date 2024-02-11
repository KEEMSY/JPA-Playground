package com.study.jpaplayground.JPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
public class User {
    @Id
    private String email;
    private String name;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User() {
    }
    public User(String email, String name, LocalDateTime createdAt) {
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
