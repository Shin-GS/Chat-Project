# 💬 Chat System

Spring Boot + Thymeleaf + HTMX + WebSocket(STOMP) 기반의 **실시간 채팅 시스템**

---

## 📌 개요

간단한 UI와 최소한의 JS로 동작하는 **MPA + HTMX 구조**의 채팅 서비스.

- 초기 렌더링: Thymeleaf
- 부분 갱신: HTMX
- 실시간 메시징: WebSocket(STOMP)

---

## ✨ 주요 기능

- 회원가입 / 로그인 🔐
- 친구 관리 👥
- 1:1 채팅 & 그룹 채팅 💬

---

## 🛠️ 기술 스택

| 영역        | 사용 기술                                                                                                                                                                                                                                                                                |
|-----------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Backend   | ![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?logo=springboot&logoColor=white) ![JPA](https://img.shields.io/badge/JPA-59666C?logo=hibernate&logoColor=white) ![Security](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white) |
| Frontend  | ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white) ![HTMX](https://img.shields.io/badge/HTMX-3366CC?logo=htmx&logoColor=white)                                                                                                               |
| Real-time | ![WebSocket](https://img.shields.io/badge/WebSocket-010101?logo=socketdotio&logoColor=white) ![STOMP](https://img.shields.io/badge/STOMP-FF6600)                                                                                                                                     |
| Database  | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)                                                                                                                                                                                                       |

---

## ⚙️ 동작 원리

- **MPA 구조**: 페이지 단위는 서버에서 Thymeleaf로 렌더링
- **HTMX**: 부분 갱신(리스트, 페이징 등)
- **WebSocket**: 실시간 메시지 송수신

---

## 📂 프로젝트 구조

```
FE
 ├─ views        # 페이지 단위 Thymeleaf HTML
 ├─ components   # HTMX 요청 대응 템플릿
 └─ fragments    # 재사용 가능한 HTML 조각

BE
 ├─ common       # 공통(상수, 유틸, 에러 등)
 ├─ config       # 설정
 ├─ domain       # Entity, Repository
 ├─ service      # 비즈니스 로직
 ├─ controller   # 컨트롤러
 │   ├─ socket   # WebSocket
 │   ├─ view     # 페이지 로딩
 │   ├─ api      # JSON 응답
 │   └─ hx       # HTMX 요청 처리
```

---

## 🚀 실행 방법

```bash
gradle bootRun
```

- DB: MySQL 실행 필요
- OpenAPI 문서: `/swagger-ui.html`

---

## 📜 라이선스

MIT
