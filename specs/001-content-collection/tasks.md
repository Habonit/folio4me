# Tasks: Backend Phase 1 - Content Collection

**Input**: Design documents from `/specs/001-content-collection/`
**Prerequisites**: plan.md (required), spec.md (required), data-model.md, contracts/, research.md, quickstart.md

**Tests**: TDD-First Development (Constitution I) - 테스트를 먼저 작성하고 실패 확인 후 구현

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Backend**: `backend/src/main/java/com/folio4me/` (소스 코드)
- **Tests**: `backend/src/test/java/com/folio4me/` (테스트 코드)
- **Resources**: `backend/src/main/resources/` (설정 파일)
- **Data**: `backend/data/{uuid}/` (런타임 데이터)

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Create backend directory structure per implementation plan at `backend/`
- [x] T002 Initialize Spring Boot 3.x project with Gradle Kotlin DSL in `backend/build.gradle.kts`
- [x] T003 [P] Configure settings.gradle.kts in `backend/settings.gradle.kts`
- [x] T004 [P] Create application.yml with base configuration in `backend/src/main/resources/application.yml`
- [x] T005 [P] Create messages_ko.properties for Korean error messages in `backend/src/main/resources/messages_ko.properties`
- [x] T006 [P] Create .env.example template in `backend/.env.example`
- [x] T007 [P] Update .gitignore to exclude .env and data/ in `backend/.gitignore`
- [x] T008 Create FolioApplication main class in `backend/src/main/java/com/folio4me/FolioApplication.java`

**Checkpoint**: Project compiles and starts with `./gradlew bootRun`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**CRITICAL**: No user story work can begin until this phase is complete

### Core Enums

- [x] T009 [P] Create ConversationStep enum (12 steps) in `backend/src/main/java/com/folio4me/model/ConversationStep.java`
- [x] T010 [P] Create SessionStatus enum in `backend/src/main/java/com/folio4me/model/SessionStatus.java`

### Portfolio Model Entities (All stories depend on these)

- [x] T011 [P] Create Meta model in `backend/src/main/java/com/folio4me/model/Meta.java`
- [x] T012 [P] Create PersonalInfo model in `backend/src/main/java/com/folio4me/model/PersonalInfo.java`
- [x] T013 [P] Create WorkExperience model in `backend/src/main/java/com/folio4me/model/WorkExperience.java`
- [x] T014 [P] Create EducationItem model in `backend/src/main/java/com/folio4me/model/EducationItem.java`
- [x] T015 [P] Create EducationSection model in `backend/src/main/java/com/folio4me/model/EducationSection.java`
- [x] T016 [P] Create Project model in `backend/src/main/java/com/folio4me/model/Project.java`
- [x] T017 [P] Create Award model in `backend/src/main/java/com/folio4me/model/Award.java`
- [x] T018 [P] Create Activity model in `backend/src/main/java/com/folio4me/model/Activity.java`
- [x] T019 [P] Create Activities model in `backend/src/main/java/com/folio4me/model/Activities.java`
- [x] T020 [P] Create Certification model in `backend/src/main/java/com/folio4me/model/Certification.java`
- [x] T021 [P] Create TechnicalSkills model in `backend/src/main/java/com/folio4me/model/TechnicalSkills.java`
- [x] T022 [P] Create About model in `backend/src/main/java/com/folio4me/model/About.java`
- [x] T023 Create Portfolio root model in `backend/src/main/java/com/folio4me/model/Portfolio.java` (depends on T011-T022)

### Session Model

- [x] T024 Create Session model in `backend/src/main/java/com/folio4me/model/Session.java` (depends on T009, T010)

### DTOs

- [x] T025 [P] Create ConversationRequest DTO in `backend/src/main/java/com/folio4me/dto/ConversationRequest.java`
- [x] T026 [P] Create ConversationResponse DTO in `backend/src/main/java/com/folio4me/dto/ConversationResponse.java`
- [x] T027 [P] Create SessionResponse DTO in `backend/src/main/java/com/folio4me/dto/SessionResponse.java`
- [x] T028 [P] Create ImageUploadResponse DTO in `backend/src/main/java/com/folio4me/dto/ImageUploadResponse.java`
- [x] T029 [P] Create ErrorResponse DTO in `backend/src/main/java/com/folio4me/dto/ErrorResponse.java`

### Exceptions

- [x] T030 [P] Create SessionNotFoundException in `backend/src/main/java/com/folio4me/exception/SessionNotFoundException.java`
- [x] T031 [P] Create InvalidInputException in `backend/src/main/java/com/folio4me/exception/InvalidInputException.java`
- [x] T032 [P] Create InvalidPeriodException in `backend/src/main/java/com/folio4me/exception/InvalidPeriodException.java`
- [x] T033 [P] Create StorageException in `backend/src/main/java/com/folio4me/exception/StorageException.java`
- [x] T034 Create GlobalExceptionHandler in `backend/src/main/java/com/folio4me/exception/GlobalExceptionHandler.java`

### Configuration

- [x] T035 [P] Create AppConfig for ObjectMapper and beans in `backend/src/main/java/com/folio4me/config/AppConfig.java`
- [x] T036 [P] Create AiConfig for AI API settings in `backend/src/main/java/com/folio4me/config/AiConfig.java`

**Checkpoint**: Foundation ready - all models compile, configuration loads

---

## Phase 3: User Story 1 - 대화형 콘텐츠 수집 (Priority: P1) 🎯 MVP

**Goal**: 기술 직군 사용자가 12단계 대화를 통해 포트폴리오 콘텐츠 수집

**Independent Test**: Step 0→11 순차 진행 후 `about.confirmed: true` 확인

### Tests for User Story 1 (TDD - Write FIRST, must FAIL)

- [x] T037 [P] [US1] Unit test for ConversationService in `backend/src/test/java/com/folio4me/service/ConversationServiceTest.java`
- [x] T038 [P] [US1] Unit test for StepHandler (12 steps) in `backend/src/test/java/com/folio4me/service/StepHandlerTest.java`
- [x] T039 [P] [US1] Integration test for conversation flow in `backend/src/test/java/com/folio4me/integration/ConversationFlowTest.java`
- [x] T040 [P] [US1] Contract test for POST /messages endpoint in `backend/src/test/java/com/folio4me/controller/ConversationControllerTest.java`

### Implementation for User Story 1

- [x] T041 [US1] Create AiService interface in `backend/src/main/java/com/folio4me/service/AiService.java`
- [x] T042 [US1] Implement AnthropicAiService in `backend/src/main/java/com/folio4me/service/AnthropicAiService.java`
- [x] T043 [US1] Create StepHandler interface in `backend/src/main/java/com/folio4me/service/StepHandler.java`
- [x] T044 [US1] Implement Step0GateHandler (직군 판별) in `backend/src/main/java/com/folio4me/service/handler/Step0GateHandler.java`
- [x] T045 [US1] Implement Step1PersonalInfoHandler in `backend/src/main/java/com/folio4me/service/handler/Step1PersonalInfoHandler.java`
- [x] T046 [US1] Implement Step2WorkExperienceHandler in `backend/src/main/java/com/folio4me/service/handler/Step2WorkExperienceHandler.java`
- [x] T047 [US1] Implement Step3EducationHandler in `backend/src/main/java/com/folio4me/service/handler/Step3EducationHandler.java`
- [x] T048 [US1] Implement Step4RepresentativeProjectsHandler in `backend/src/main/java/com/folio4me/service/handler/Step4RepresentativeProjectsHandler.java`
- [x] T049 [US1] Implement Step5ProjectsHandler in `backend/src/main/java/com/folio4me/service/handler/Step5ProjectsHandler.java`
- [x] T050 [US1] Implement Step6AwardsHandler in `backend/src/main/java/com/folio4me/service/handler/Step6AwardsHandler.java`
- [x] T051 [US1] Implement Step7MajorActivitiesHandler in `backend/src/main/java/com/folio4me/service/handler/Step7MajorActivitiesHandler.java`
- [x] T052 [US1] Implement Step8OtherActivitiesHandler in `backend/src/main/java/com/folio4me/service/handler/Step8OtherActivitiesHandler.java`
- [x] T053 [US1] Implement Step9CertificationsHandler in `backend/src/main/java/com/folio4me/service/handler/Step9CertificationsHandler.java`
- [x] T054 [US1] Implement Step10TechnicalSkillsHandler in `backend/src/main/java/com/folio4me/service/handler/Step10TechnicalSkillsHandler.java`
- [x] T055 [US1] Implement Step11AboutHandler in `backend/src/main/java/com/folio4me/service/handler/Step11AboutHandler.java`
- [x] T056 [US1] Create StepHandlerRegistry in `backend/src/main/java/com/folio4me/service/StepHandlerRegistry.java`
- [x] T057 [US1] Implement ConversationService in `backend/src/main/java/com/folio4me/service/ConversationService.java`
- [x] T058 [US1] Implement ConversationController POST /messages in `backend/src/main/java/com/folio4me/controller/ConversationController.java`

**Checkpoint**: User can complete 12-step conversation, all tests pass

---

## Phase 4: User Story 2 - 세션 관리 및 데이터 저장 (Priority: P1)

**Goal**: UUID 세션 발급, 디렉토리 생성, 실시간 JSON 저장

**Independent Test**: 세션 생성 시 `data/{uuid}/portfolio.json` 생성 확인

### Tests for User Story 2 (TDD - Write FIRST, must FAIL)

- [ ] T059 [P] [US2] Unit test for SessionService in `backend/src/test/java/com/folio4me/service/SessionServiceTest.java`
- [ ] T060 [P] [US2] Unit test for StorageService in `backend/src/test/java/com/folio4me/service/StorageServiceTest.java`
- [ ] T061 [P] [US2] Contract test for session endpoints in `backend/src/test/java/com/folio4me/controller/SessionControllerTest.java`
- [ ] T062 [P] [US2] Integration test for session persistence in `backend/src/test/java/com/folio4me/integration/SessionPersistenceTest.java`

### Implementation for User Story 2

- [x] T063 [US2] Implement StorageService (file I/O with ReentrantLock) in `backend/src/main/java/com/folio4me/service/StorageService.java`
- [x] T064 [US2] Implement SessionService (UUID, in-memory session map) in `backend/src/main/java/com/folio4me/service/SessionService.java`
- [x] T065 [US2] Implement SessionController POST /sessions in `backend/src/main/java/com/folio4me/controller/SessionController.java`
- [x] T066 [US2] Implement SessionController GET /sessions/{id} in `backend/src/main/java/com/folio4me/controller/SessionController.java`
- [x] T067 [US2] Implement SessionController DELETE /sessions/{id} in `backend/src/main/java/com/folio4me/controller/SessionController.java`
- [x] T068 [US2] Implement ConversationController GET /sessions/{id}/portfolio in `backend/src/main/java/com/folio4me/controller/ConversationController.java`
- [x] T069 [US2] Add real-time save trigger to ConversationService in `backend/src/main/java/com/folio4me/service/ConversationService.java`

**Checkpoint**: Sessions persist to file, can resume after restart

---

## Phase 5: User Story 3 - 입력 검증 및 완성도 관리 (Priority: P2)

**Goal**: 필수 필드 검증, 기간 형식 검증, confirmed 상태 관리

**Independent Test**: 필수 필드 누락 시 재질문, 완료 시 confirmed: true

### Tests for User Story 3 (TDD - Write FIRST, must FAIL)

- [ ] T070 [P] [US3] Unit test for ValidationService in `backend/src/test/java/com/folio4me/service/ValidationServiceTest.java`
- [ ] T071 [P] [US3] Unit test for PeriodValidator in `backend/src/test/java/com/folio4me/validation/PeriodValidatorTest.java`
- [ ] T072 [P] [US3] Unit test for NullInputHandler in `backend/src/test/java/com/folio4me/service/NullInputHandlerTest.java`

### Implementation for User Story 3

- [ ] T073 [P] [US3] Create ValidPeriod annotation in `backend/src/main/java/com/folio4me/validation/ValidPeriod.java`
- [ ] T074 [US3] Implement PeriodValidator in `backend/src/main/java/com/folio4me/validation/PeriodValidator.java`
- [ ] T075 [US3] Implement ValidationService in `backend/src/main/java/com/folio4me/service/ValidationService.java`
- [ ] T076 [US3] Add null input handling ("없습니다", "패스") to StepHandlers in `backend/src/main/java/com/folio4me/service/handler/`
- [ ] T077 [US3] Add confirmed state management to StepHandlers in `backend/src/main/java/com/folio4me/service/handler/`
- [ ] T078 [US3] Add re-question logic for missing fields to ConversationService in `backend/src/main/java/com/folio4me/service/ConversationService.java`

**Checkpoint**: Invalid inputs trigger re-question, all confirmed states tracked

---

## Phase 6: User Story 4 - 주제 이탈 감지 및 대화 복귀 (Priority: P3)

**Goal**: 주제 이탈 감지, 정중한 복귀 유도

**Independent Test**: 무관한 입력 시 이탈 응답 및 원래 질문 재제시

### Tests for User Story 4 (TDD - Write FIRST, must FAIL)

- [ ] T079 [P] [US4] Unit test for TopicRelevanceChecker in `backend/src/test/java/com/folio4me/service/TopicRelevanceCheckerTest.java`
- [ ] T080 [P] [US4] Integration test for topic divergence handling in `backend/src/test/java/com/folio4me/integration/TopicDivergenceTest.java`

### Implementation for User Story 4

- [ ] T081 [US4] Implement TopicRelevanceChecker in `backend/src/main/java/com/folio4me/service/TopicRelevanceChecker.java`
- [ ] T082 [US4] Add topic divergence detection to ConversationService in `backend/src/main/java/com/folio4me/service/ConversationService.java`
- [ ] T083 [US4] Add polite redirect responses to messages_ko.properties in `backend/src/main/resources/messages_ko.properties`

**Checkpoint**: Off-topic inputs redirect back to current step

---

## Phase 7: User Story 5 - About 자기소개 AI 생성 및 수정 (Priority: P2)

**Goal**: AI가 3문장 자기소개 초안 생성, 수정-확인 사이클

**Independent Test**: Step 11에서 AI 초안 생성, 수정 요청 시 재생성, 확정 시 confirmed: true

### Tests for User Story 5 (TDD - Write FIRST, must FAIL)

- [ ] T084 [P] [US5] Unit test for AboutGenerationService in `backend/src/test/java/com/folio4me/service/AboutGenerationServiceTest.java`
- [ ] T085 [P] [US5] Integration test for about generation flow in `backend/src/test/java/com/folio4me/integration/AboutGenerationFlowTest.java`

### Implementation for User Story 5

- [ ] T086 [US5] Implement AboutGenerationService in `backend/src/main/java/com/folio4me/service/AboutGenerationService.java`
- [ ] T087 [US5] Create AI prompt templates for about generation in `backend/src/main/resources/prompts/about-generation.txt`
- [ ] T088 [US5] Integrate AboutGenerationService into Step11AboutHandler in `backend/src/main/java/com/folio4me/service/handler/Step11AboutHandler.java`
- [ ] T089 [US5] Add modification request detection to Step11AboutHandler in `backend/src/main/java/com/folio4me/service/handler/Step11AboutHandler.java`
- [ ] T090 [US5] Add Phase 1 completion logic (meta.status = "complete") in `backend/src/main/java/com/folio4me/service/ConversationService.java`

**Checkpoint**: AI generates about, modifications work, completion sets status

---

## Phase 8: Image Upload (Cross-cutting for US1/US2)

**Goal**: 증명사진 업로드 기능 (Step 1에서 사용)

**Independent Test**: 이미지 업로드 시 파일 저장 및 경로 반환

### Tests for Image Upload

- [ ] T091 [P] Unit test for ImageUploadService in `backend/src/test/java/com/folio4me/service/ImageUploadServiceTest.java`
- [ ] T092 [P] Contract test for POST /images endpoint in `backend/src/test/java/com/folio4me/controller/ImageControllerTest.java`

### Implementation for Image Upload

- [ ] T093 Implement ImageUploadService in `backend/src/main/java/com/folio4me/service/ImageUploadService.java`
- [ ] T094 Implement ImageController POST /sessions/{id}/images in `backend/src/main/java/com/folio4me/controller/ImageController.java`
- [ ] T095 Integrate image upload into Step1PersonalInfoHandler in `backend/src/main/java/com/folio4me/service/handler/Step1PersonalInfoHandler.java`

**Checkpoint**: Image upload works, path saved to personalInfo.profileImage

---

## Phase 9: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T096 [P] Add comprehensive Javadoc to all public methods in `backend/src/main/java/com/folio4me/`
- [ ] T097 [P] Add logging throughout services in `backend/src/main/java/com/folio4me/service/`
- [ ] T098 [P] Create test fixtures and helper classes in `backend/src/test/java/com/folio4me/fixtures/`
- [ ] T099 Run all tests and verify 70%+ unit test coverage in `backend/`
- [ ] T100 Run quickstart.md validation script to verify setup in `backend/`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies - can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion - BLOCKS all user stories
- **User Stories (Phase 3-7)**: All depend on Foundational phase completion
  - US1 and US2 can proceed in parallel (both P1)
  - US3, US4, US5 depend on US1/US2 core services
- **Image Upload (Phase 8)**: Depends on US1/US2 infrastructure
- **Polish (Phase 9)**: Depends on all user stories being complete

### User Story Dependencies

| User Story | Priority | Dependencies | Can Parallel With |
|------------|----------|--------------|-------------------|
| US1 (대화형 수집) | P1 | Foundational | US2 |
| US2 (세션/저장) | P1 | Foundational | US1 |
| US3 (입력 검증) | P2 | US1, US2 | US5 |
| US4 (주제 이탈) | P3 | US1 | - |
| US5 (About 생성) | P2 | US1, US2 | US3 |

### Within Each User Story

1. Tests (TDD) MUST be written and FAIL before implementation
2. Models/Interfaces before Services
3. Services before Controllers
4. Core implementation before integration
5. Story complete before moving to next priority

---

## Parallel Execution Examples

### Phase 2 (Foundational) - All models in parallel:

```bash
# Launch all model tasks together:
Task T011-T022: Create all Portfolio sub-models in parallel
Task T025-T029: Create all DTOs in parallel
Task T030-T033: Create all Exceptions in parallel
```

### Phase 3 (User Story 1) - Tests first, then handlers:

```bash
# Step 1: Write all tests (must fail)
Task T037-T040: Write all US1 tests in parallel

# Step 2: Implement handlers (some can be parallel)
Task T044-T055: Implement Step handlers (can be parallelized if no shared deps)
```

### Parallel User Stories (P1):

```bash
# Developer A: User Story 1 (대화형 수집)
# Developer B: User Story 2 (세션/저장)
# Both start after Phase 2 completion
```

---

## Implementation Strategy

### MVP First (User Stories 1 + 2 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational (CRITICAL - blocks all stories)
3. Complete Phase 3: User Story 1 (대화형 수집)
4. Complete Phase 4: User Story 2 (세션/저장)
5. **STOP and VALIDATE**: Test full conversation flow
6. Deploy/demo if ready

### Incremental Delivery

1. Setup + Foundational → Foundation ready
2. Add US1 + US2 → Test independently → MVP! (12-step conversation + persistence)
3. Add US3 → Test validation → Deploy (input quality)
4. Add US5 → Test about generation → Deploy (AI-generated about)
5. Add US4 → Test topic divergence → Deploy (polished UX)
6. Add Image Upload → Test images → Deploy (full feature)

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (대화형 수집)
   - Developer B: User Story 2 (세션/저장)
3. After US1+US2:
   - Developer A: User Story 3 (입력 검증)
   - Developer B: User Story 5 (About 생성)
4. Developer A: User Story 4 (주제 이탈)
5. Both: Image Upload + Polish

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- **TDD Required**: Verify tests FAIL before implementing (Red-Green-Refactor)
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- All paths are relative to `backend/` directory
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
