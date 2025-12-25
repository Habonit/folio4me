# Quickstart: Frontend Web Implementation

**Feature**: 002-frontend-web
**Date**: 2025-12-24

## Prerequisites

- Node.js 18+
- pnpm (front/ 프로젝트의 패키지 매니저)
- 백엔드 서버 실행 중 (localhost:8080)

---

## 1. Setup

```bash
# front/ 디렉토리로 이동
cd front

# 의존성 설치
pnpm install

# 개발 서버 실행
pnpm dev
```

개발 서버: `http://localhost:5173`

---

## 2. Environment

```bash
# front/.env
PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

---

## 3. Development Workflow

### 3.1 백엔드 먼저 실행

```bash
# backend/ 디렉토리에서
./gradlew bootRun
```

### 3.2 프론트엔드 실행

```bash
# front/ 디렉토리에서
pnpm dev
```

### 3.3 테스트 플로우

1. 브라우저에서 `http://localhost:5173` 접속
2. "새 세션 시작" 클릭 → 세션 생성
3. 채팅 화면에서 대화 진행
4. Step 0~11 순서대로 진행
5. 포트폴리오 미리보기 확인

---

## 4. Project Structure

```
front/
├── src/
│   ├── lib/
│   │   ├── api/              # API 클라이언트
│   │   │   ├── config.ts
│   │   │   ├── session.ts
│   │   │   └── index.ts
│   │   ├── components/
│   │   │   ├── chat/         # 채팅 컴포넌트
│   │   │   ├── layout/       # 레이아웃
│   │   │   └── preview/      # 미리보기
│   │   ├── stores/           # Svelte stores
│   │   └── types/            # TypeScript 타입
│   └── routes/
│       ├── +layout.svelte
│       ├── +page.svelte      # 메인 (세션 생성)
│       └── chat/
│           └── [sessionId]/
│               └── +page.svelte
├── package.json
└── vite.config.ts
```

---

## 5. Key Commands

| Command | Description |
|:--------|:------------|
| `pnpm dev` | 개발 서버 실행 |
| `pnpm build` | 프로덕션 빌드 |
| `pnpm preview` | 빌드 미리보기 |
| `pnpm check` | TypeScript 타입 체크 |
| `pnpm lint` | ESLint 실행 |

---

## 6. Testing

### 6.1 수동 테스트 (MVP)

MVP 단계에서는 수동 테스트로 백엔드 연동 확인:

1. 세션 생성 → 채팅 시작
2. Step별 메시지 전송
3. 응답 확인 및 Step 진행
4. 포트폴리오 데이터 미리보기

### 6.2 자동 테스트 (추후)

```bash
# Unit tests
pnpm test

# E2E tests
pnpm test:e2e
```

---

## 7. Troubleshooting

### CORS 에러

백엔드에서 CORS 설정 확인:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

### API 연결 실패

1. 백엔드 서버 실행 여부 확인
2. `PUBLIC_API_BASE_URL` 환경변수 확인
3. 네트워크 탭에서 요청/응답 확인

---

## 8. Next Steps

1. `pnpm dev`로 개발 서버 실행
2. 기존 Open WebUI 코드 정리 (불필요 기능 제거)
3. API 클라이언트 구현
4. 채팅 UI 구현
5. 백엔드 연동 테스트
