# Feature Specification: Frontend Web Implementation

**Feature Branch**: `002-frontend-web`
**Created**: 2025-12-24
**Status**: Draft
**Input**: Frontend web implementation for content collection using Open WebUI (front/*)

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 대화형 포트폴리오 콘텐츠 수집 (Priority: P1)

사용자가 웹 채팅 인터페이스를 통해 12단계 대화 흐름으로 포트폴리오에 필요한 정보(개인정보, 경력, 프로젝트, 기술 스택 등)를 입력하고, 실시간으로 수집된 내용을 확인할 수 있습니다.

**Why this priority**: 핵심 기능으로, 백엔드 Phase 1(001-content-collection)과 연동하여 실제 포트폴리오 데이터를 수집하는 MVP 기능입니다.

**Independent Test**: 사용자가 로그인 후 새 프로젝트를 생성하고, Step 0~11까지 대화를 진행하여 about.confirmed = true가 될 때까지 테스트 가능합니다.

**Acceptance Scenarios**:

1. **Given** 로그인된 사용자가 새 프로젝트를 생성했을 때, **When** 채팅 화면에 진입하면, **Then** Step 0(희망 직무 확인) 초기 프롬프트가 표시됩니다.
2. **Given** 사용자가 채팅 중일 때, **When** 메시지를 입력하고 Send 버튼을 클릭하면, **Then** AI 응답이 표시되고 Step이 진행됩니다.
3. **Given** Step이 진행 중일 때, **When** Split 모드로 전환하면, **Then** 우측 영역에 수집된 데이터가 마크다운으로 렌더링됩니다.
4. **Given** 모든 Step이 완료되었을 때, **When** about.confirmed = true가 되면, **Then** Phase 1 완료 상태가 표시됩니다.

---

### User Story 2 - 프로젝트 관리 대시보드 (Priority: P2)

사용자가 메인 보드에서 포트폴리오 프로젝트를 생성, 조회, 수정, 삭제하고 진행 상태를 확인할 수 있습니다.

**Why this priority**: 프로젝트 관리는 대화 수집 기능의 진입점이며, 여러 포트폴리오를 관리할 수 있게 합니다.

**Independent Test**: 사용자가 로그인 후 프로젝트 목록을 보고, 새 프로젝트 생성/삭제/제목 수정이 가능한지 테스트합니다.

**Acceptance Scenarios**:

1. **Given** 로그인된 사용자가 있을 때, **When** Main 화면에 진입하면, **Then** 프로젝트 목록이 테이블 형태로 표시됩니다.
2. **Given** Main 화면에서, **When** Create 버튼을 클릭하면, **Then** 새 프로젝트가 생성되고 목록에 추가됩니다.
3. **Given** 프로젝트가 존재할 때, **When** 해당 행을 클릭하면, **Then** Chat 화면으로 이동합니다.
4. **Given** 프로젝트를 선택했을 때, **When** Delete 버튼을 클릭하면, **Then** 선택된 프로젝트가 삭제됩니다.

---

### User Story 3 - 인증 및 기초 인프라 (Priority: P3)

사용자가 로그인/회원가입을 통해 시스템에 접근하고, API Key를 설정할 수 있습니다.

**Why this priority**: 인증은 모든 기능의 전제 조건이지만, Open WebUI의 기존 인증 시스템을 활용하므로 커스터마이징만 필요합니다.

**Independent Test**: 회원가입 → 로그인 → API Key 설정 → 로그아웃 플로우를 독립적으로 테스트합니다.

**Acceptance Scenarios**:

1. **Given** 미인증 사용자가 있을 때, **When** Login 화면에서 올바른 자격증명을 입력하면, **Then** Main 화면으로 이동합니다.
2. **Given** Login 화면에서, **When** Sign up 버튼을 클릭하면, **Then** 회원가입 화면으로 이동합니다.
3. **Given** 설정 버튼을 클릭했을 때, **When** API Key를 입력하고 Save하면, **Then** 키가 저장됩니다.

---

### Edge Cases

- 네트워크 연결이 끊어졌을 때 채팅 메시지 전송 실패 시 재시도 옵션을 제공합니다.
- 세션이 만료되었을 때 사용자에게 재로그인 안내 메시지를 표시합니다.
- 잘못된 형식의 입력(기간, 이메일 등)이 들어왔을 때 인라인 에러 메시지를 표시합니다.
- 이미지 업로드 실패 시 사용자에게 에러 메시지와 재시도 버튼을 제공합니다.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: 시스템은 사용자 로그인/로그아웃 기능을 제공해야 합니다
- **FR-002**: 시스템은 회원가입 기능을 제공해야 합니다
- **FR-003**: 사용자는 프로젝트를 생성/조회/수정/삭제할 수 있어야 합니다
- **FR-004**: 사용자는 채팅 인터페이스를 통해 메시지를 전송하고 AI 응답을 받을 수 있어야 합니다
- **FR-005**: 시스템은 12단계(Step 0~11) 대화 흐름을 지원해야 합니다
- **FR-006**: 시스템은 Single/Split 모드 전환을 지원해야 합니다
- **FR-007**: Split 모드에서 수집된 데이터를 마크다운으로 실시간 렌더링해야 합니다
- **FR-008**: 사이드바에 현재 진행 중인 Step을 표시해야 합니다
- **FR-009**: 프로젝트 진행률을 계산하여 표시해야 합니다
- **FR-010**: 증명사진 이미지 업로드 기능을 제공해야 합니다
- **FR-011**: OpenAI API Key 입력 및 저장 기능을 제공해야 합니다

### Key Entities

- **User**: 시스템 사용자, 인증 정보 및 API Key 보유
- **Project**: 포트폴리오 프로젝트, UUID로 식별, 진행 상태(phase, step, status) 포함
- **Session**: 백엔드와의 대화 세션, 프로젝트와 1:1 매핑
- **Message**: 채팅 메시지, 사용자/AI 구분, 타임스탬프 포함

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 사용자가 로그인 후 새 프로젝트 생성까지 30초 이내에 완료할 수 있습니다
- **SC-002**: 채팅 메시지 전송 후 AI 응답이 5초 이내에 표시됩니다
- **SC-003**: Step 진행 시 Split 모드로 자동 전환이 1초 이내에 완료됩니다
- **SC-004**: 12단계 대화 흐름을 모두 완료하는 데 평균 15분 이내 소요됩니다
- **SC-005**: 프로젝트 진행률이 Step 완료 시마다 실시간으로 업데이트됩니다
- **SC-006**: 90% 이상의 사용자가 도움 없이 첫 포트폴리오 콘텐츠 수집을 완료할 수 있습니다

## Scope

### In Scope

- 인증 화면 커스터마이징 (Login, Sign up)
- 프로젝트 관리 대시보드 (Main Board)
- 채팅 인터페이스 (Single/Split 모드)
- 백엔드 Phase 1 API 연동 (세션 관리, 메시지 전송, 포트폴리오 조회)
- 사이드바 Step 네비게이션
- 마크다운 미리보기
- 이미지 업로드 (증명사진)

### Out of Scope

- Phase 2 (스타일 선택) - 별도 피처로 구현
- Phase 3 (포트폴리오 생성) - 별도 피처로 구현
- 모바일 반응형 디자인 - 추후 개선
- 다국어 지원 - 추후 개선

## Assumptions

- Open WebUI의 기존 인증 시스템을 활용합니다
- 백엔드 API(001-content-collection)가 구현되어 있습니다
- 사용자는 현대적인 웹 브라우저(Chrome, Firefox, Safari, Edge)를 사용합니다

## Dependencies

- **Backend**: 001-content-collection (세션 관리, 대화 처리 API)
- **Base Framework**: Open WebUI (SvelteKit, Tailwind CSS)
