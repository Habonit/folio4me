# folio4me

> AI 기반 포트폴리오 자동 생성 서비스

## 소개

대화형 인터페이스를 통해 사용자의 경력, 프로젝트, 기술 역량을 수집하고 맞춤형 포트폴리오를 자동 생성합니다. 12단계 대화 흐름으로 체계적인 정보 수집을 지원합니다.

---

## 기술 스택

| 구분 | 기술 |
|:-----|:-----|
| **Backend** | Java 17, Spring Boot 3.2.x, Gradle (Kotlin DSL) |
| **Frontend** | SvelteKit, TypeScript, Tailwind CSS (Open WebUI 기반) |
| **AI** | Claude API (Anthropic) |
| **버전** | v0.1.0 |

---

## 프로젝트 구조

```
folio4me/
├── backend/                    # Spring Boot 백엔드
│   ├── src/main/java/com/folio4me/
│   │   ├── config/            # 설정 클래스
│   │   ├── controller/        # REST 컨트롤러
│   │   ├── dto/               # 요청/응답 DTO
│   │   ├── exception/         # 예외 처리
│   │   ├── model/             # 도메인 모델
│   │   ├── service/           # 비즈니스 로직
│   │   └── validation/        # 입력 검증
│   └── build.gradle.kts
├── front/                      # SvelteKit 프론트엔드
│   ├── src/
│   │   ├── lib/
│   │   │   ├── api/           # API 클라이언트
│   │   │   ├── components/    # UI 컴포넌트
│   │   │   ├── stores/        # Svelte 스토어
│   │   │   └── types/         # TypeScript 타입
│   │   └── routes/
│   │       ├── auth/          # 인증 화면
│   │       ├── chat/          # 채팅 화면
│   │       └── folio4me/      # 프로젝트 관리
│   └── package.json
├── docs/                       # 문서
│   ├── api_spec.md            # API 명세서
│   ├── error_spec.md          # 에러 명세서
│   └── project/               # 프로젝트 문서
└── specs/                      # 피처 스펙
    ├── 001-content-collection/ # 백엔드 콘텐츠 수집
    └── 002-frontend-web/       # 프론트엔드 웹
```

---

## 주요 기능

### Phase 1: 콘텐츠 수집 (현재)

| 기능 | 설명 | 상태 |
|:-----|:-----|:----:|
| 세션 관리 | 세션 생성/조회/삭제 | ✅ |
| 12단계 대화 흐름 | 희망 직무 → 개인정보 → 경력 → ... → 자기소개 | ✅ |
| 포트폴리오 데이터 수집 | 구조화된 JSON 형태로 저장 | ✅ |
| 웹 채팅 인터페이스 | Single/Split 모드 지원 | ✅ |
| 마크다운 미리보기 | 수집된 데이터 실시간 렌더링 | ✅ |

### 12단계 대화 흐름

| Step | 단계명 | 수집 정보 |
|:----:|:------|:---------|
| 0 | 희망 직무 확인 | 서비스 대상 여부 (기술직군) |
| 1 | 기본 개인정보 | 이름, GitHub, 이메일, 증명사진 |
| 2 | 이력 | 경력 정보 (복수 입력) |
| 3 | 학력 | 학력 정보 (복수 입력) |
| 4 | 대표 프로젝트 | 주요 프로젝트 (복수 입력) |
| 5 | 일반 프로젝트 | 기타 프로젝트 (복수 입력) |
| 6 | 수상 경력 | 수상 내역 (복수 입력) |
| 7 | 주요 대외활동 | 주요 활동 (복수 입력) |
| 8 | 그 외 대외활동 | 기타 활동 (복수 입력) |
| 9 | 자격증 | 자격증/시험 (복수 입력) |
| 10 | 기술 역량 | Strong, Knowledgeable 분류 |
| 11 | 자기소개 | 3문장 자기소개 |

---

## 시작하기

### 사전 요구사항

- Java >= 17
- Node.js >= 18
- Gradle >= 8.x

### Backend 실행

```bash
cd backend

# 환경 설정
cp .env.example .env
# .env 파일에 ANTHROPIC_API_KEY 설정

# 빌드 및 실행
./gradlew build
./gradlew bootRun

# 테스트
./gradlew test
```

### Frontend 실행

```bash
cd front

# 환경 설정
cp .env.example .env
# PUBLIC_API_BASE_URL 확인 (기본: http://localhost:8080/api/v1)

# 의존성 설치 및 실행
npm install
npm run dev
```

### 환경변수

**Backend (.env)**

| 변수명 | 설명 | 필수 |
|:-------|:-----|:----:|
| `ANTHROPIC_API_KEY` | Claude API 키 | ✅ |
| `DATA_DIR` | 데이터 저장 경로 | |

**Frontend (.env)**

| 변수명 | 설명 | 기본값 |
|:-------|:-----|:-------|
| `PUBLIC_API_BASE_URL` | 백엔드 API URL | `http://localhost:8080/api/v1` |

---

## API 엔드포인트

| API | Method | Endpoint | 설명 |
|:----|:-------|:---------|:-----|
| 세션 생성 | POST | `/api/v1/sessions` | 새 세션 생성 |
| 세션 조회 | GET | `/api/v1/sessions/{id}` | 세션 상태 조회 |
| 세션 삭제 | DELETE | `/api/v1/sessions/{id}` | 세션 삭제 |
| 메시지 전송 | POST | `/api/v1/sessions/{id}/messages` | 대화 메시지 전송 |
| 포트폴리오 조회 | GET | `/api/v1/sessions/{id}/portfolio` | 수집된 포트폴리오 조회 |
| 프롬프트 조회 | GET | `/api/v1/sessions/{id}/prompt` | 현재 단계 프롬프트 조회 |

---

## 에러 코드

| 에러 코드 | HTTP | 설명 |
|:----------|:-----|:-----|
| `SESSION_NOT_FOUND` | 404 | 세션을 찾을 수 없음 |
| `INVALID_INPUT` | 400 | 유효하지 않은 입력 |
| `INVALID_PERIOD` | 400 | 유효하지 않은 기간 형식 |
| `STORAGE_ERROR` | 500 | 저장소 오류 |
| `INTERNAL_SERVER_ERROR` | 500 | 서버 내부 오류 |

---

## 변경 이력

### v0.1.0 (2025-12-24)

**Backend (001-content-collection)**
- 12단계 대화 흐름 구현
- 세션 관리 API (CRUD)
- 포트폴리오 데이터 모델
- 에러 핸들링 및 검증

**Frontend (002-frontend-web)**
- 채팅 인터페이스 (Single/Split 모드)
- API 클라이언트 구현
- Svelte 스토어 (Session, Chat, Portfolio)
- 마크다운 미리보기

[전체 변경 이력](docs/version_change_log.md)

---

## 문서

- [API 명세서](docs/api_spec.md)
- [에러 명세서](docs/error_spec.md)
- [대화 흐름 명세서](docs/project/conversation-flow-spec.md)
- [포트폴리오 데이터 스키마](docs/project/portfolio-data-schema.md)
- [Frontend 스펙](specs/002-frontend-web/spec.md)

---

## 로드맵

| Phase | 기능 | 상태 |
|:------|:-----|:----:|
| Phase 1 | 콘텐츠 수집 | ✅ 완료 |
| Phase 2 | 스타일 선택 | 📋 예정 |
| Phase 3 | 포트폴리오 생성 | 📋 예정 |

---

## 라이선스

MIT License
