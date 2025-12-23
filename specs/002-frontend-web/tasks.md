# Tasks: Frontend Web Implementation

**Input**: Design documents from `/specs/002-frontend-web/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/

**Tests**: MVP 단계에서는 수동 테스트 우선. 자동 테스트는 포함하지 않음. (plan.md Constitution Check 참조)

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Web app**: `front/src/` (SvelteKit project)

---

## Phase 1: Setup (프로젝트 초기화)

**Purpose**: 기존 Open WebUI 코드 정리 및 MVP 프로젝트 구조 설정

- [x] T001 기존 front/ 코드 분석 및 불필요한 기능 목록 작성
- [x] T002 front/src/lib/ 하위에 새 디렉토리 구조 생성 (api/, components/, stores/, types/)
- [x] T003 [P] front/.env 파일 생성 및 PUBLIC_API_BASE_URL 설정
- [x] T004 [P] front/src/lib/types/folio4me.ts에 TypeScript 타입 정의 (data-model.md 기반)

---

## Phase 2: Foundational (API 클라이언트 및 상태 관리)

**Purpose**: 모든 User Story에서 사용하는 공통 인프라

**⚠️ CRITICAL**: 이 Phase 완료 전까지 User Story 작업 불가

- [x] T005 front/src/lib/api/config.ts 생성 - API 설정 및 base URL
- [x] T006 [P] front/src/lib/api/error.ts 생성 - 에러 파싱 및 ErrorCodes 상수
- [x] T007 front/src/lib/api/session.ts 생성 - 세션 API 클라이언트 (create, get, delete)
- [x] T008 front/src/lib/api/conversation.ts 생성 - 대화 API 클라이언트 (sendMessage, getPortfolio, getCurrentPrompt)
- [x] T009 front/src/lib/api/index.ts 생성 - API 클라이언트 통합 export
- [x] T010 [P] front/src/lib/stores/folio4me/session.ts 생성 - 세션 상태 스토어
- [x] T011 [P] front/src/lib/stores/folio4me/chat.ts 생성 - 채팅 상태 스토어
- [x] T012 [P] front/src/lib/stores/folio4me/portfolio.ts 생성 - 포트폴리오 상태 스토어
- [x] T013 front/src/lib/stores/folio4me/index.ts 생성 - 스토어 통합 export

**Checkpoint**: API 클라이언트 및 상태 관리 인프라 완료 - User Story 구현 시작 가능

---

## Phase 3: User Story 1 - 대화형 포트폴리오 콘텐츠 수집 (Priority: P1) 🎯 MVP

**Goal**: 채팅 인터페이스로 12단계 대화 흐름을 통해 포트폴리오 정보 수집

**Independent Test**:
1. 브라우저에서 localhost:5173 접속
2. "새 세션 시작" 버튼 클릭
3. 채팅 화면에서 Step 0~11 순서대로 대화 진행
4. Split 모드에서 수집된 데이터 확인
5. about.confirmed = true 시 완료 표시 확인

### Implementation for User Story 1

- [x] T014 [P] [US1] front/src/lib/components/chat/ChatMessage.svelte 생성 - 메시지 버블 컴포넌트
- [x] T015 [P] [US1] front/src/lib/components/chat/ChatInput.svelte 생성 - 메시지 입력 컴포넌트
- [x] T016 [US1] front/src/lib/components/chat/ChatArea.svelte 생성 - 채팅 영역 컴포넌트 (T014, T015 조합)
- [x] T017 [P] [US1] front/src/lib/components/folio4me/layout/Sidebar.svelte 생성 - Step 진행 표시 사이드바
- [x] T018 [P] [US1] front/src/lib/components/layout/Header.svelte 생성 - 헤더 (모드 전환 버튼)
- [x] T019 [P] [US1] front/src/lib/components/preview/MarkdownPreview.svelte 생성 - 포트폴리오 마크다운 미리보기
- [x] T020 [US1] front/src/routes/chat/[sessionId]/+page.svelte 생성 - 채팅 페이지 (Single/Split 모드)
- [x] T021 [US1] front/src/routes/chat/[sessionId]/+page.ts 생성 - 채팅 페이지 load 함수 (세션 조회, 프롬프트 조회)
- [x] T022 [US1] 채팅 페이지에 에러 처리 및 로딩 상태 UI 추가 (ChatArea.svelte에 포함)
- [x] T023 [US1] Step 완료 시 progressPercentage 업데이트 및 UI 반영 (+page.svelte에 포함)

**Checkpoint**: User Story 1 완료 - 채팅으로 12단계 대화 흐름 테스트 가능

---

## Phase 4: User Story 2 - 프로젝트 관리 대시보드 (Priority: P2)

**Goal**: 메인 화면에서 세션(프로젝트) 생성/조회/삭제

**Independent Test**:
1. 브라우저에서 localhost:5173 접속
2. 프로젝트 목록 확인 (초기에는 빈 목록)
3. "새 세션 생성" 버튼 클릭 → 목록에 추가
4. 프로젝트 행 클릭 → 채팅 화면으로 이동
5. 삭제 버튼 클릭 → 목록에서 제거

### Implementation for User Story 2

- [x] T024 [US2] front/src/lib/stores/folio4me/projects.ts 생성 - 프로젝트 목록 스토어 (세션 목록 관리)
- [x] T025 [P] [US2] front/src/lib/components/dashboard/ProjectTable.svelte 생성 - 프로젝트 목록 테이블
- [x] T026 [P] [US2] front/src/lib/components/dashboard/CreateButton.svelte 생성 - 새 프로젝트 생성 버튼
- [x] T027 [US2] front/src/routes/folio4me/+page.svelte 생성 - 메인 대시보드 페이지 (프로젝트 목록, 생성/삭제)
- [x] T028 [US2] 기존 +layout.svelte 활용 - 별도 수정 불필요
- [x] T029 [US2] 프로젝트 삭제 확인 다이얼로그 추가 (+page.svelte에 포함)

**Checkpoint**: User Story 2 완료 - 대시보드에서 프로젝트 관리 가능

---

## Phase 5: User Story 3 - 인증 및 기초 인프라 (Priority: P3)

**Goal**: 로그인/회원가입 및 API Key 설정 (Open WebUI 기존 시스템 활용)

**Independent Test**:
1. 로그인 화면 접속
2. 회원가입 → 로그인 → 메인 화면 이동
3. 설정에서 API Key 입력/저장
4. 로그아웃 → 로그인 화면으로 이동

### Implementation for User Story 3

- [ ] T030 [US3] Open WebUI 기존 인증 코드 분석 및 활용 가능 부분 파악
- [ ] T031 [P] [US3] front/src/routes/login/+page.svelte 수정 - 로그인 화면 커스터마이징
- [ ] T032 [P] [US3] front/src/routes/signup/+page.svelte 수정 - 회원가입 화면 커스터마이징
- [ ] T033 [US3] front/src/lib/stores/auth.ts 생성 - 인증 상태 스토어
- [ ] T034 [US3] front/src/lib/components/settings/ApiKeyInput.svelte 생성 - API Key 입력 컴포넌트
- [ ] T035 [US3] 인증 상태에 따른 라우트 가드 구현 (비인증 시 로그인 리다이렉트)

**Checkpoint**: User Story 3 완료 - 인증 플로우 동작

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: 전체 기능 통합 및 품질 개선

- [ ] T036 [P] Tailwind CSS 스타일링 정리 및 일관성 확보
- [ ] T037 [P] 에러 메시지 한글화 및 사용자 친화적 메시지 적용
- [ ] T038 네트워크 에러 시 재시도 옵션 UI 구현
- [ ] T039 로딩 스피너 및 스켈레톤 UI 추가
- [ ] T040 quickstart.md 기반 수동 테스트 실행 및 검증
- [ ] T041 불필요한 Open WebUI 코드 정리 (사용하지 않는 컴포넌트/라우트 제거)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-5)**: All depend on Foundational phase completion
- **Polish (Phase 6)**: Depends on all user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Foundational 완료 후 시작 가능 - 🎯 MVP
- **User Story 2 (P2)**: Foundational 완료 후 시작 가능 - US1과 독립적으로 테스트 가능
- **User Story 3 (P3)**: Foundational 완료 후 시작 가능 - MVP에서는 인증 없이 진행 가능

### Within Each User Story

- Components before pages
- Core implementation before integration
- Basic functionality before error handling

### Parallel Opportunities

- T003, T004 can run in parallel (Setup phase)
- T006, T010, T011, T012 can run in parallel (Foundational phase)
- T014, T015, T017, T018, T019 can run in parallel (US1 components)
- T025, T026 can run in parallel (US2 components)
- T031, T032 can run in parallel (US3 auth pages)
- T036, T037 can run in parallel (Polish phase)

---

## Parallel Example: User Story 1

```bash
# Launch all independent components for US1 together:
Task: "front/src/lib/components/chat/ChatMessage.svelte 생성"
Task: "front/src/lib/components/chat/ChatInput.svelte 생성"
Task: "front/src/lib/components/layout/Sidebar.svelte 생성"
Task: "front/src/lib/components/layout/Header.svelte 생성"
Task: "front/src/lib/components/preview/MarkdownPreview.svelte 생성"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (T001-T004)
2. Complete Phase 2: Foundational (T005-T013)
3. Complete Phase 3: User Story 1 (T014-T023)
4. **STOP and VALIDATE**: 채팅으로 12단계 대화 테스트
5. 백엔드 API 연동 확인

### Incremental Delivery

1. Setup + Foundational → 인프라 준비
2. Add User Story 1 → 채팅 기능 테스트 → MVP 완료!
3. Add User Story 2 → 대시보드 추가
4. Add User Story 3 → 인증 추가 (선택적)
5. Polish → 품질 개선

### Suggested MVP Scope

**🎯 MVP = Phase 1 + Phase 2 + Phase 3 (User Story 1)**

MVP에서는 인증(US3)과 대시보드(US2)를 건너뛰고, 직접 채팅 URL로 접속하여 백엔드 API 테스트에 집중.

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- MVP 목적: 백엔드 API 테스트 - 채팅 인터페이스만 구현해도 목적 달성
- Open WebUI 기존 코드 활용 시 라이선스 확인 필요
- 수동 테스트 시 백엔드 서버(localhost:8080) 실행 필수
