# Research: Frontend Web Implementation

**Feature**: 002-frontend-web
**Date**: 2025-12-24

## 1. 기술 스택 결정

### Decision: SvelteKit + Tailwind CSS (Open WebUI 기반)

**Rationale**: front/* 디렉토리가 Open WebUI를 기반으로 하고 있으며, 사용자가 이 스택을 그대로 활용하기를 원함.

**기존 스택 분석 (front/package.json)**:
- Framework: SvelteKit 2.5.27, Svelte 5.0
- Styling: Tailwind CSS 4.0
- Build: Vite 5.4.14
- Testing: Vitest, Cypress
- Language: TypeScript 5.5.4

**Alternatives considered**:
- React/Next.js: 기존 코드베이스 재사용 불가
- Vue/Nuxt: 기존 코드베이스 재사용 불가

---

## 2. MVP 범위 결정

### Decision: 백엔드 테스트용 최소 기능만 구현

**Rationale**: 사용자가 "나머지 모든 기능은 다 제거해도 됩니다"라고 명시함. Open WebUI의 복잡한 기능(다국어, OAuth, 다중 모델 등)은 불필요.

**포함할 기능**:
1. 채팅 인터페이스 (백엔드 API 연동)
2. 세션 생성/관리
3. 메시지 전송/수신
4. Step 진행 표시
5. Split 모드 (마크다운 미리보기)

**제거할 기능 (Open WebUI 원본 대비)**:
- 인증/회원가입 (MVP에서 불필요)
- 다국어 지원 (i18n)
- 다중 LLM 모델 지원
- 파일 업로드/문서 처리
- 음성 입력/출력
- 모델 관리 기능
- 관리자 기능
- 프로젝트 대시보드 (간소화)

---

## 3. 프로젝트 구조 결정

### Decision: front/* 디렉토리에 새로운 최소화된 앱 구축

**Rationale**: 기존 Open WebUI 코드가 너무 복잡하여 수정보다 필요한 부분만 새로 작성하는 것이 효율적임.

**구조**:
```
front/
├── src/
│   ├── lib/
│   │   ├── components/
│   │   │   ├── chat/
│   │   │   │   ├── ChatInput.svelte      # 메시지 입력
│   │   │   │   ├── ChatMessage.svelte    # 메시지 버블
│   │   │   │   └── ChatArea.svelte       # 채팅 영역
│   │   │   ├── layout/
│   │   │   │   ├── Sidebar.svelte        # 사이드바 (Step 표시)
│   │   │   │   └── Header.svelte         # 헤더
│   │   │   └── preview/
│   │   │       └── MarkdownPreview.svelte # 마크다운 미리보기
│   │   ├── api/
│   │   │   └── session.ts                # 백엔드 API 클라이언트
│   │   ├── stores/
│   │   │   ├── session.ts                # 세션 상태
│   │   │   └── chat.ts                   # 채팅 상태
│   │   └── types/
│   │       └── index.ts                  # TypeScript 타입 정의
│   └── routes/
│       ├── +layout.svelte                # 기본 레이아웃
│       ├── +page.svelte                  # 메인 (세션 생성)
│       └── chat/
│           └── [sessionId]/
│               └── +page.svelte          # 채팅 페이지
├── package.json
└── vite.config.ts
```

---

## 4. 백엔드 API 연동

### Decision: fetch API 직접 사용

**Rationale**: 단순한 REST API 호출에 axios 등 추가 라이브러리 불필요.

**연동할 API (docs/api_spec.md 기준)**:

| API | 용도 |
|:----|:----|
| POST /sessions | 새 세션 생성 |
| GET /sessions/{id} | 세션 정보 조회 |
| DELETE /sessions/{id} | 세션 삭제 |
| POST /sessions/{id}/messages | 메시지 전송 |
| GET /sessions/{id}/portfolio | 포트폴리오 데이터 조회 |
| GET /sessions/{id}/prompt | 현재 Step 프롬프트 조회 |

**Base URL**: `http://localhost:8080/api/v1`

---

## 5. 상태 관리

### Decision: Svelte Store 사용

**Rationale**: SvelteKit의 내장 상태 관리로 충분하며, Redux/MobX 등 외부 라이브러리 불필요.

**Store 구조**:
- `sessionStore`: 현재 세션 정보 (id, currentStep, status)
- `chatStore`: 채팅 메시지 목록
- `portfolioStore`: 포트폴리오 데이터 (미리보기용)

---

## 6. UI/UX 결정

### Decision: Single 모드만 우선 구현, Split 모드는 선택적

**Rationale**: MVP에서 핵심은 백엔드 테스트이므로 채팅 기능에 집중.

**구현 우선순위**:
1. P1: 채팅 인터페이스 (메시지 송수신)
2. P2: Step 진행 표시 (사이드바)
3. P3: Split 모드 (마크다운 미리보기)

---

## 7. 테스팅 전략

### Decision: 수동 테스트 우선, 자동 테스트는 추후

**Rationale**: MVP 목적이 백엔드 테스트이므로 프론트엔드 자동 테스트는 후순위.

**테스트 방법**:
1. 백엔드 서버 실행 (localhost:8080)
2. 프론트엔드 개발 서버 실행 (localhost:5173)
3. 브라우저에서 직접 테스트

---

## Summary

| 항목 | 결정 |
|:----|:----|
| Framework | SvelteKit + Tailwind CSS |
| Scope | 최소 MVP (채팅 + Step 표시) |
| 구조 | 새로운 최소화된 앱 |
| API | fetch 직접 사용 |
| 상태 관리 | Svelte Store |
| UI | Single 모드 우선 |
| 테스트 | 수동 테스트 |
