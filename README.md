# SpringShoppingMall
2024년도 전자정부프레임워크 기말과제
쇼핑몰 제작 프로젝트 202045801 강병준

### 프로젝트 개발 동기 및 목표
전자정부 프레임워크의 기술 스택을 이론 및 예제 실습을 통해 학습한 다음, 실제로 어떻게 동작하는 지를 이해하고자 기말 과제로 쇼핑몰 프로젝트를 구현.

### 프로젝트 환경
Programming Languages:	Java SDK 23
Front-End:	HTML, CSS, JS, BootStrap, Thymeleaf v3.3.0
Back-End:	Spring Boot v3.3.5, Spring Security 6, Spring Data JPA
Databases:	Hibernate, MySQL
Services:	None (Local)
![image](https://github.com/user-attachments/assets/0a0a5485-c553-4129-a261-5f69ff029697)

### DB 모델링
![image](https://github.com/user-attachments/assets/6d4444ee-7d97-4ed4-abc5-9f96bcc6363e)

### API 명세서
Function	Method	EndPoint	
회원 가입 페이지	Get	/members/new
회원 가입	Post	/members/new
로그인 페이지	Get	/members/login
로그인	Post	/members/login
로그아웃	Get	/member/logout
장바구니 담기	Post	/cart
장바구니 페이지	Get	/cart
장바구니 상품 수정	Patch	/cartItem/{cartItemId}
장바구니 상품 제거	Delete	/cartItem/{cartItemId}
장바구니 상품 주문	Post	/cart/orders
상품 등록 페이지	Get	/admin/item/new
상품 등록	Post	/admin/item/new
상품 조회	Get	/item/{itemId}, /admin/items", "/admin/items/{page}, /admin/item/{itemId}
상품 수정	Post	/admin/item/{itemId}
주문하기	Post	/order
주문 내역 페이지	Get	/orders, /orders/{page}
주문 취소	Post	/order/{orderID}/cancel
메인 페이지 조회	Get	/
에러 페이지	Get	/error

### 진행과정
-	https://rocreamsnote.notion.site/1373557098428009902dec48dde64c2d?pvs=74

### 참고한 레퍼런스
- https://velog.io/@codren/Spring-Boot-쇼핑몰-프로젝트
