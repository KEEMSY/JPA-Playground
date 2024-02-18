package com.study.jpaplayground.JPA.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // @ManyToOne: 다대일 관계 매핑
    // @JoinColumn: 외래 키를 매핑할 때 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private double price;

    @Column(name = "order_date") // @Column: 테이블의 컬럼명 매핑
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) // @Enumerated: 자바의 enum 타입을 사용하는 경우 지정
    private OrderStatus status;
}
