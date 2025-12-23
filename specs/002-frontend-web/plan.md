# Implementation Plan: Frontend Web Implementation

**Branch**: `002-frontend-web` | **Date**: 2025-12-24 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/002-frontend-web/spec.md`

## Summary

백엔드 Phase 1 (001-content-collection) API를 테스트하기 위한 최소 MVP 프론트엔드 구현.
SvelteKit + Tailwind CSS 기반으로 채팅 인터페이스와 Step 진행 표시 기능만 구현합니다.

## Technical Context

**Language/Version**: TypeScript 5.5.4
**Primary Dependencies**: SvelteKit 2.5.27, Svelte 5.0, Tailwind CSS 4.0, Vite 5.4.14
**Storage**: N/A (백엔드 의존)
**Testing**: Vitest (수동 테스트 우선, 자동 테스트 추후)
**Target Platform**: Modern Web Browsers (Chrome, Firefox, Safari, Edge)
**Project Type**: web (frontend only for MVP)
**Performance Goals**: AI 응답 5초 이내 표시
**Constraints**: 백엔드 API 의존, 인증 없음 (MVP)
**Scale/Scope**: 단일 사용자 테스트용

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| 원칙 | 상태 | 비고 |
|:----|:-----|:-----|
| I. TDD-First Development | ⚠️ 부분 적용 | MVP는 수동 테스트 우선, 추후 자동화 |
| II. Code Style & Convention | ✅ 준수 | TypeScript + camelCase/PascalCase |
| III. Git Branch & Commit Convention | ✅ 준수 | feature/002-frontend-web |
| IV. Error Handling & Graceful Degradation | ✅ 준수 | API 에러 처리 구현 |
| V. Phase-Based Development | ✅ 준수 | Phase 1 백엔드 테스트용 |
| VI. Simplicity First | ✅ 준수 | 불필요 기능 모두 제거 |
| VII. Environment & Security | ✅ 준수 | .env 파일 사용 |

## Project Structure

### Documentation (this feature)

```text
specs/002-frontend-web/
├── plan.md              # This file
├── research.md          # 기술 스택 결정
├── data-model.md        # 프론트엔드 데이터 모델
├── quickstart.md        # 개발 환경 설정
├── contracts/           # API 클라이언트 계약
│   └── api-client.md
├── checklists/
│   └── requirements.md
└── tasks.md             # Phase 2 output (/speckit.tasks command)
```

### Source Code (repository root)

```text
front/
├── src/
│   ├── lib/
│   │   ├── api/                    # API 클라이언트
│   │   │   ├── config.ts
│   │   │   ├── session.ts
│   │   │   └── index.ts
│   │   ├── components/
│   │   │   ├── chat/               # 채팅 컴포넌트
│   │   │   │   ├── ChatInput.svelte
│   │   │   │   ├── ChatMessage.svelte
│   │   │   │   └── ChatArea.svelte
│   │   │   ├── layout/             # 레이아웃
│   │   │   │   ├── Sidebar.svelte
│   │   │   │   └── Header.svelte
│   │   │   └── preview/            # 미리보기
│   │   │       └── MarkdownPreview.svelte
│   │   ├── stores/                 # Svelte stores
│   │   │   ├── session.ts
│   │   │   ├── chat.ts
│   │   │   └── portfolio.ts
│   │   └── types/                  # TypeScript 타입
│   │       └── index.ts
│   └── routes/
│       ├── +layout.svelte
│       ├── +page.svelte            # 메인 (세션 생성)
│       └── chat/
│           └── [sessionId]/
│               └── +page.svelte    # 채팅 페이지
├── package.json
├── vite.config.ts
└── .env
```

**Structure Decision**: front/ 디렉토리에 SvelteKit 기반 최소 MVP 구현.
기존 Open WebUI 코드는 참조용으로만 활용하고, 필요한 부분만 새로 작성.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| TDD 부분 적용 | MVP 목적이 백엔드 테스트 | 프론트엔드 자동 테스트는 백엔드 안정화 후 진행 |
