# Feature Specification: Backend Phase 1 - Content Collection

**Feature Branch**: `001-content-collection`
**Created**: 2025-12-23
**Status**: Draft
**Input**: User description: "Backend Phase 1: Content Collection - 사용자와 대화를 통해 포트폴리오 콘텐츠를 수집하는 백엔드 기능 개발"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - 기술 직군 사용자의 대화형 콘텐츠 수집 (Priority: P1)

기술 직군 사용자가 AI 챗봇과 대화하며 포트폴리오에 필요한 12개 Step의 콘텐츠를 순차적으로 입력합니다. 각 Step에서 사용자 입력을 받아 검증하고, 완료 시 다음 단계로 자동 전환됩니다.

**Why this priority**: 핵심 비즈니스 기능으로, 이 기능 없이는 포트폴리오 생성이 불가능합니다. 전체 서비스의 근간이 되는 콘텐츠 수집 파이프라인입니다.

**Independent Test**: Step 0부터 Step 11까지 순차적으로 대화를 진행하여 `portfolio.json` 파일이 정상 생성되는지 확인. `about.confirmed: true` 상태가 되면 Phase 1 완료.

**Acceptance Scenarios**:

1. **Given** 새 세션이 시작됨, **When** 사용자가 희망 직무를 "백엔드 개발자"로 입력, **Then** 시스템이 기술 직군으로 판단하고 Step 1으로 이동
2. **Given** Step 1 진행 중, **When** 사용자가 이름/GitHub/이메일/증명사진을 입력, **Then** `personalInfo` 섹션이 `confirmed: true`로 저장됨
3. **Given** 모든 Step 완료 후, **When** 사용자가 About 문장을 확정, **Then** `portfolio.json`의 `about.confirmed: true`가 되고 Phase 1 완료

---

### User Story 2 - 세션 관리 및 데이터 저장 (Priority: P1)

사용자가 새 포트폴리오 작성을 시작하면 UUID가 발급되고 전용 디렉토리가 생성됩니다. 대화 진행 중 각 Step 완료 시 데이터가 실시간으로 저장되어, 중간에 이탈해도 데이터가 유지됩니다.

**Why this priority**: 데이터 영속성 없이는 콘텐츠 수집이 무의미합니다. 사용자 경험과 데이터 안정성의 근간입니다.

**Independent Test**: 세션 시작 시 `data/{uuid}/` 디렉토리와 `portfolio.json` 파일 생성 확인. 각 Step 완료 시 파일 내용 업데이트 확인.

**Acceptance Scenarios**:

1. **Given** 사용자가 포트폴리오 작성 시작, **When** 세션이 생성됨, **Then** UUID v4 형식의 식별자 발급 및 `data/{uuid}/` 디렉토리 생성
2. **Given** Step 2 진행 중, **When** 사용자가 경력 정보 입력 완료, **Then** `portfolio.json`의 `workExperience` 배열에 항목 추가 및 즉시 저장
3. **Given** 사용자가 브라우저를 닫음, **When** 다시 접속하여 같은 세션 재개, **Then** 마지막 저장된 상태부터 계속 진행 가능

---

### User Story 3 - 입력 검증 및 완성도 관리 (Priority: P2)

사용자 입력이 누락되거나 형식이 잘못된 경우 시스템이 재질문을 통해 완성도를 보장합니다. 각 항목의 `confirmed` 상태를 추적하여 미완성 항목이 있으면 다음 단계로 진행하지 않습니다.

**Why this priority**: 데이터 품질 보장은 중요하지만, 기본 수집 기능이 먼저 구현되어야 합니다.

**Independent Test**: 필수 필드가 누락된 입력 시 재질문 발생 확인. 모든 필드가 채워지면 `confirmed: true` 설정 확인.

**Acceptance Scenarios**:

1. **Given** Step 4 대표 프로젝트 입력 중, **When** 사용자가 담당 직무 없이 제출, **Then** 시스템이 "담당 직무를 입력해주세요" 재질문
2. **Given** 기간 입력 시, **When** 잘못된 형식(예: "작년부터") 입력, **Then** 시스템이 YYYYMMDD~YYYYMMDD 형식 안내
3. **Given** 사용자가 "없습니다" 입력, **When** 선택 항목인 경우, **Then** 해당 필드를 `null`로 저장하고 `confirmed: true` 처리

---

### User Story 4 - 주제 이탈 감지 및 대화 복귀 (Priority: P3)

사용자가 현재 Step과 무관한 질문이나 잡담을 하면 시스템이 이를 감지하고, 정중하게 원래 대화 주제로 복귀시킵니다.

**Why this priority**: 사용자 경험 향상 기능으로, 핵심 수집 기능 완료 후 구현해도 됩니다.

**Independent Test**: 현재 Step과 무관한 입력 시 주제 이탈 응답 확인. 원래 질문을 다시 제시하는지 확인.

**Acceptance Scenarios**:

1. **Given** Step 2 이력 입력 중, **When** 사용자가 "오늘 날씨 어때?" 입력, **Then** "해당 질문에는 답변드리기 어렵습니다. 재직했던 회사가 있으신가요?" 응답
2. **Given** Step 10 기술 스택 입력 중, **When** 사용자가 이전 Step 수정 요청, **Then** 시스템이 현재 Step 완료 후 수정 가능함을 안내

---

### User Story 5 - About 자기소개 AI 생성 및 수정 (Priority: P2)

Step 11에서 지금까지 입력한 내용을 기반으로 AI가 3문장 자기소개 초안을 생성합니다. 사용자가 수정을 요청하면 반영하고, 확정할 때까지 수정-확인 사이클을 반복합니다.

**Why this priority**: 단순 입력이 아닌 AI 생성 로직이 필요하여 별도 구현이 필요합니다.

**Independent Test**: Step 11 진입 시 AI가 자기소개 초안 생성 확인. 수정 요청 시 반영되는지 확인. "네, 좋아요" 입력 시 `about.confirmed: true` 설정 확인.

**Acceptance Scenarios**:

1. **Given** Step 10 완료 후 Step 11 진입, **When** 사용자가 강조 포인트 입력, **Then** AI가 포트폴리오 내용 기반 3문장 초안 생성
2. **Given** 초안 제시됨, **When** 사용자가 "두 번째 문장 수정해줘" 요청, **Then** AI가 수정된 버전 재생성
3. **Given** 수정된 초안 제시됨, **When** 사용자가 "네, 좋아요" 확정, **Then** `about.confirmed: true` 설정 및 Phase 1 완료 안내

---

### Edge Cases

- 사용자가 Step 0에서 비기술 직군(예: 마케터)을 입력하면 서비스 종료 안내와 함께 세션 종료
- 기간 형식이 현재 진행 중인 경우 `YYYYMMDD~present` 형식 허용
- 빈 배열 `[]` 섹션(예: 경력 없음)은 포트폴리오 렌더링 시 해당 섹션 전체 숨김
- 필드 레벨 `null` 값(예: 관련 링크 없음)은 해당 필드만 숨김
- 증명사진 업로드 시 부적절한 이미지(예: 반려동물 사진) 경고 메시지 표시

## Requirements *(mandatory)*

### Functional Requirements

**세션 및 데이터 관리**
- **FR-001**: 시스템 MUST 새 세션 시작 시 UUID v4 형식의 고유 식별자 발급
- **FR-002**: 시스템 MUST 세션 시작 시 `data/{uuid}/` 디렉토리 생성
- **FR-003**: 시스템 MUST 각 Step 완료 시 `portfolio.json` 파일 즉시 저장
- **FR-004**: 시스템 MUST `portfolio-data-schema.md` 스키마에 맞는 JSON 구조로 데이터 저장

**대화 흐름 관리**
- **FR-005**: 시스템 MUST Step 0~11까지 12단계 대화 흐름 순차 진행 지원
- **FR-006**: 시스템 MUST 각 Step별 정의된 필드만 수집 (conversation-flow-spec.md 기준)
- **FR-007**: 시스템 MUST 현재 Step 완료 전까지 다음 Step 진입 차단
- **FR-008**: 시스템 MUST Step 0에서 비기술 직군 판단 시 서비스 종료 안내

**입력 검증**
- **FR-009**: 시스템 MUST 필수 필드 누락 시 재질문 발생
- **FR-010**: 시스템 MUST 기간 입력 형식(YYYYMMDD~YYYYMMDD 또는 YYYYMMDD~present) 검증
- **FR-011**: 시스템 MUST 사용자가 거부 의사 표현 시(없습니다, 패스) `null` 저장 허용
- **FR-012**: 시스템 MUST 각 항목의 `confirmed` 상태를 false → true로 관리

**주제 이탈 처리**
- **FR-013**: 시스템 MUST 현재 Step과 무관한 입력 감지 시 원래 주제로 복귀 유도
- **FR-014**: 시스템 MUST 주제 이탈 시 현재 진행 중인 Step 명과 질문 재안내

**About 생성**
- **FR-015**: 시스템 MUST Step 11에서 지금까지 수집된 내용 요약 제시
- **FR-016**: 시스템 MUST 사용자 강조 포인트 기반 3문장 자기소개 초안 생성
- **FR-017**: 시스템 MUST 사용자 수정 요청 시 재생성 반복
- **FR-018**: 시스템 MUST 사용자 확정 시 `about.confirmed: true` 설정

**증명사진 처리**
- **FR-019**: 시스템 MUST Step 1에서 이미지 업로드 기능 제공
- **FR-020**: 시스템 MUST 업로드된 이미지 경로를 `personalInfo.profileImage`에 저장

**반복 입력 처리**
- **FR-021**: 시스템 MUST Step 2~9에서 반복 입력 지원 (경력, 프로젝트 등 복수 항목)
- **FR-022**: 시스템 MUST 각 항목 입력 후 "추가로 더 있으신가요?" 질문
- **FR-023**: 시스템 MUST 관련 링크 필드는 여러 개 입력 허용 (쉼표 구분)

**완료 조건**
- **FR-024**: 시스템 MUST `about.confirmed: true` 시 Phase 1 완료 처리
- **FR-025**: 시스템 MUST Phase 1 완료 시 `meta.status: "complete"` 설정

### Key Entities

- **Session**: 사용자 세션 정보 (UUID, 생성시간, 현재 Step, 상태)
- **Portfolio**: 포트폴리오 콘텐츠 데이터 (`portfolio.json` 구조 전체)
- **PersonalInfo**: 기본 개인정보 (이름, GitHub, 이메일, 증명사진)
- **WorkExperience**: 경력 정보 (회사명, 기간, 직무, 직급, 관련 링크)
- **Education**: 학력 정보 (학교, 학위, 학과, 기간, 노출 여부)
- **Project**: 프로젝트 정보 (대표/일반 구분, 프로젝트명, 기간, 직무, 기술스택, 링크)
- **Award**: 수상 정보 (수상명, 기간, 내용, 기관, 링크)
- **Activity**: 대외활동 정보 (major/minor 구분, 제목, 기간, 내용, 링크)
- **Certification**: 자격증 정보 (자격증명, 기간, 기관, 링크)
- **TechnicalSkills**: 기술 역량 (strong/knowledgeable 분류)
- **About**: 자기소개 (3문장, 확정 여부)

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 사용자가 Step 0부터 Step 11까지 전체 대화를 5분 내에 완료 가능 (평균 시나리오 기준)
- **SC-002**: 90% 이상의 사용자가 첫 시도에서 모든 Step을 완료
- **SC-003**: 필수 필드 누락률 0% (시스템이 모든 필수 필드 입력 검증)
- **SC-004**: 주제 이탈 발생 시 3회 이내에 원래 대화로 복귀
- **SC-005**: 생성된 `portfolio.json` 파일이 100% 스키마 규격 준수
- **SC-006**: About 자기소개 초안 생성 후 평균 2회 이내 수정으로 확정
- **SC-007**: 세션 중단 후 재접속 시 100% 데이터 복구 및 이어서 진행 가능
