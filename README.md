# ShoesTrade

C2C e커머스의 플랫폼 중 하나인 KREAM을 모티브로 거래 물품을 신발로 한정시킨 중개 거래 API 서버

## 프로젝트 목표
 - KREAM과 같은 중개 거래 플랫폼 구현
 - 객체 지향의 원리를 적용하여 재사용성이 높고 유지보수가 용이한 코드 작성
 - 좋은 코드를 작성하기 위한 주기적인 코드 리뷰
 - 서버와 클라이언트 간의 역할을 구분하고 의존성을 줄이기 위해 RESTful한 코드 작성

## 기술 스택
 - JAVA
 - Spring Boot
 - Spring Security & JWT
 - MySQL
 - JPA
 - QueryDSL
 - JUnit
 - AWS

## ERD
<img width="1206" alt="image" src="https://user-images.githubusercontent.com/89503136/165758379-370e3459-fb59-4169-8c6c-02f91bae5e9b.png">

## 패키지 구조
<pre>
<code>
src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── study
    │   │           └── shoestrade
    │   │               ├── common
    │   │               │   ├── annotation
    │   │               │   ├── aop
    │   │               │   ├── config
    │   │               │   │   ├── auditing
    │   │               │   │   ├── jwt
    │   │               │   │   ├── login
    │   │               │   │   └── security
    │   │               │   │       ├── accessDeniedHandler
    │   │               │   │       ├── authenticationEntryPoint
    │   │               │   │       ├── code
    │   │               │   │       ├── dto
    │   │               │   │       └── member
    │   │               │   ├── response
    │   │               │   └── result
    │   │               ├── controller
    │   │               ├── domain
    │   │               │   ├── interest
    │   │               │   ├── mailAuth
    │   │               │   ├── member
    │   │               │   ├── product
    │   │               │   └── trade
    │   │               ├── dto
    │   │               │   ├── account
    │   │               │   ├── address
    │   │               │   │   └── response
    │   │               │   ├── admin
    │   │               │   ├── brand
    │   │               │   ├── interest
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   ├── member
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   ├── product
    │   │               │   │   ├── request
    │   │               │   │   └── response
    │   │               │   └── trade
    │   │               │       ├── request
    │   │               │       └── response
    │   │               ├── exception
    │   │               │   ├── address
    │   │               │   ├── brand
    │   │               │   ├── interest
    │   │               │   ├── mailAuth
    │   │               │   ├── member
    │   │               │   ├── product
    │   │               │   ├── token
    │   │               │   └── trade
    │   │               ├── repository
    │   │               │   ├── brand
    │   │               │   ├── interest
    │   │               │   ├── jdbc
    │   │               │   ├── member
    │   │               │   ├── product
    │   │               │   └── trade
    │   │               └── service
    │   │                   ├── admin
    │   │                   ├── brand
    │   │                   ├── interest
    │   │                   ├── member
    │   │                   ├── product
    │   │                   └── trade
    │   └── resources
    └── test
        └── java
            └── com
                └── study
                    └── shoestrade
                        ├── repository
                        └── service
</code>
</pre>

## 기능
### 회원

- 회원가입 시 아이디, 비밀번호, 이름, 주소, 신발사이즈가 필요하며 아이디 중복 체크가 필요하다.
- 회원가입 시 휴대폰 인증을 해야한다.
- 이메일 찾기, 비밀번호 찾기가 가능하다.
- 회원 등급을 브론즈, 실버, 골드, 플래티넘, 다이아몬드로 나뉜다.
- 회원 등급에 따라 포인트 적립 수준이 다르다.
- 마이페이지에서 구매 내역, 판매 내역, 관심 상품, 회원 등급을 확인할 수 있다.
- 마이페이지에서 프로필 수정 및 포인트를 확인 할 수 있다.
- 마이페이지에서 판매 정산 계좌를 등록 및 수정할 수 있다.
- 회원 탈퇴가 가능하다.

### 관리자

- 관리자는 상품을 등록할 수 있다.
- 관리자는 브랜드를 등록, 수정, 삭제를 할 수 있다.
- 모든 회원 정보를 조회할 수 있다.
- 회원을 정지 시킬 수 있다.

### 상품

- 상품은 이름, 모델 번호, 사이즈, 컬러, 발매가가 필요하다.
- 상품의 사진을 확인 할 수 있다.
- 상품은 사이즈별 구매가, 판매가가 필요하다.
- 상품은 관심 상품 횟수를 가진다.
- 로그인을 하지 않아도 상품은 조회은 가능하다.
- 상품의 시세 변동을 확인 할 수 있다.
- 상품의 사이즈별 최근 판매 내역을 확인 할 수 있다.

### 브랜드

- 브랜드는 중복 조회가 가능하다.

### 장바구니

- 장바구니를 등록, 삭제, 조회를 할 수 있다.

### 거래

- 로그인을 해야 거래가 가능하다.
- 판매 전에 판매 정산 계좌가 등록되어 있어야 한다.
- 상품을 즉시 구매 또는 구매 입찰을 할 수 있다.
- 상품을 즉시 판매 또는 판매 입찰을 할 수 있다.

### 결제

- 결제 방법에는 간편 결제, 신용카드, 실시간 계좌이체, 가상 계좌가 있다.
- 결제 취소가 불가능하다.

### 배송

- 배송 주소가 필요하다.
- 배송 상태를 확인 할 수 있다.

### 검수

- 검수 상태를 확인 할 수 있다.
- 검수 상태에는 상품 도착, 검수 중, 진품/가품이 있다.
- 검수 가품 판명 시 회원을 정지시킨다.

<!--
## 커밋 컨벤션
- add : 새로운 기능 추가
- fix : 버그 수정
- refactor : 코드 리펙토링
- test : 테스트 코드 추가
- 영어로 작성할시 과거 시제를 사용하지 않고 명령어로 작성하며, 첫 글자는 대문자를 사용한다.
-->
