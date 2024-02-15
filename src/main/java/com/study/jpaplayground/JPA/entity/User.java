package com.study.jpaplayground.JPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;


/*
[ 객체와 테이블 매핑 어노테이션 ]
- @Entity: JPA에서 테이블에 매핑할 클래스에 붙임. 해당 클래스는 엔티티라 부른다.
  - 기본생성자(default constructor)는 필수
  - final class, enum, interface, inner class에 사용 불가. final 필드 사용 불가
-> runtime시에 javassist에 의해 Entity의 서브클래스 생성하기 때문. 클래스 상속 불가하면 안됨

- @Table: 엔티티와 매핑할 테이블명을 지정
- @Id: 기본키(primary Key) 매핑
- @Column: 테이블의 컬럼명 매핑. 지정안하는 경우 객체 필드명과 동일하게 지정
- @Enumerated: 자바의 enum 타입을 사용하는 경우 지정
- @Temporal: 날짜 타입(Data, Calendar) 매핑시 사용. (DATE, TIME, TIMESTAMP)
- @Lob: CLOB, BLOB 타입과 매핑시
- @Transient: 테이블 컬럼에 매핑되지 않는 필드에 지정.
- @Access: JPA가 엔티티 데이터에 접근하는 방식 지정. FILED가 기본값.
  - 필드(FILED): 직접 필드에 접근
  -Getter(PROPERTY): getter를 통해 접근
- @GeneratedValue: 기본 키 생성 전략을 지정. IDENTITY, SEQUENCE, TABLE 등이 있음
- @Embedded, @Embeddable: 임베디드 타입, 즉 값 타입을 정의할 때 사용. @Embedded는 사용하는 곳에, @Embeddable은 클래스에 선언
- @AttributeOverride, @AttributeOverrides: 임베디드 타입에 선언된 매핑정보를 재정의할 때 사용
- @AssociationOverride, @AssociationOverrides: 연관성을 재정의할 때 사용

[ 연관관계 매핑 어노테이션 ]
- 양방향, 단방향 을 고민해보는게 좋음
- 연관 관계의 주인은 외래 키가 있는 곳으로 설정하는 것이 가장 좋다. 이는 데이터베이스 관점에서 가장 효율적임
- @ManyToMany 어노테이션은 실제로는 잘 사용되지 않음. 그 이유는 관계형 데이터베이스에서는 정규화를 위해 보통 N:M 관계를
  1:N, N:1 관계로 분해하여 사용하기 때문. 따라서, 실제 개발에서는 중간 테이블을 엔티티로 매핑하고, @ManyToMany 대신에
  @OneToMany, @ManyToOne을 사용하는 것이 일반적 임

- 각각 1:1, 1:N, N:1, N:M 관계를 매핑할 때 사용
  - @OneToOne: 1:1 관계 매핑
  - @OneToMany(mappedBy): 1:N 관계 매핑, mappedBy 속성으로 연관관계의 주인을 명시, 1:N 관계는 항상 N:1 관계의 반대 방향이다. 참조 대상이 N 건이므로 Collection, List, Set, Map 자료구조를 사용한다.
  - @ManyToOne: N:1 관계 매핑, 다대일 관계의 반대는 항상 일대다 관계이다. 연관 관계의 주인은 항상 N 쪽이 갖는다.(N 쪽에 외래키가 있음)
  - @ManyToMany: N:M 관계 매핑
  - @JoinColumn(name): 외래 키 매핑시 사용, name 속성으로 매핑할 외래 키 이름을 지정
  - @JoinTable: 다대다 관계에서 연결 테이블을 지정, 별도 엔티티 생성 없이 매핑

[ 상속관계 매핑 어노테이션 ]
- @MappedSuperclass: 공통 매핑 정보가 필요할 때 사용하는 어노테이션. 해당 어노테이션을 사용한 클래스는 테이블과 매핑되지 않음
 */

@Getter @Setter
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
