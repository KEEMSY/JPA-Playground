# JPA Playground

## 공부 내용


## 로컬 환경 구성

### 사용 기술 스택

- Spring Boot 3.2.2
- Java 17

### MySQL 설치

- Docker 로 MySQL 8.0 컨테이너 실행

**컨테이너 시작**

```shell
docker create --name mysql8 -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8.0.27
docker start mysql8
```

**컨테이너 접속, MySQL 접속**

```shell
docker exec -it mysql8 bash
mysql -u root -p
```

**MySQL 설정**

```sql
CREATE DATABASE IF NOT EXISTS jpabegin CHARACTER SET utf8mb4;
    
CREATE USER 'jpauser'@'localhost' IDENTIFIED BY 'jpapass';
CREATE USER 'jpauser'@'%' IDENTIFIED BY 'jpapass';
    
GRANT ALL PRIVILEGES ON jpabegin.* TO 'jpauser'@'localhost';
GRANT ALL PRIVILEGES ON jpabegin.* TO 'jpauser'@'%';

FLUSH PRIVILEGES;
```