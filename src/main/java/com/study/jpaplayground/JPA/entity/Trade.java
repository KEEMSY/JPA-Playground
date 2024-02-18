package com.study.jpaplayground.JPA.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trades")
public class Trade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @OneToOne
    @JoinColumn(name = "offer_order_id", referencedColumnName = "order_id")
    private Order offerOrder;

    @OneToOne
    @JoinColumn(name = "request_order_id", referencedColumnName = "order_id")
    private Order requestOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus status;
}

enum TradeStatus {
    PROPOSED, ACCEPTED, DECLINED
}