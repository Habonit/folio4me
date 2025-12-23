# Implementation Plan: Backend Phase 1 - Content Collection

**Branch**: `001-content-collection` | **Date**: 2025-12-23 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-content-collection/spec.md`

## Summary

사용자와 AI 챗봇 간의 12단계 대화를 통해 포트폴리오 콘텐츠를 수집하는 백엔드 시스템 구현. Step 0(직군 판별)부터 Step 11(About 자기소개 생성)까지 순차적으로 진행하며, 각 단계별 데이터를 `portfolio.json`으로 저장. 세션 기반 상태 관리와 입력 검증, 주제 이탈 감지 기능 포함.

## Technical Context

**Language/Version**: Java 17+
**Primary Dependencies**: Spring Boot 3.x (REST API), Jackson (JSON 처리), OpenAI/Anthropic SDK (AI 대화)
**Storage**: File-based JSON (`data/{uuid}/portfolio.json`)
**Testing**: JUnit 5 + Mockito (Given-When-Then 패턴)
**Target Platform**: Linux server / Docker container
**Project Type**: Web application (backend-first, frontend 추후 개발)
**Build Tool**: Gradle
**Performance Goals**: 대화 응답 < 3초, 동시 세션 100개 지원
**Constraints**: 세션당 메모리 < 10MB, 파일 저장 즉시 반영
**Scale/Scope**: MVP 기준 동시 사용자 100명, 12단계 대화 흐름

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. TDD-First Development | ✅ PASS | JUnit 5 + Mockito, Red-Green-Refactor 적용 |
| II. Code Style & Convention | ✅ PASS | Java 17+ 타입 힌트, Javadoc 필수, 한글 주석 |
| III. Git Branch & Commit | ✅ PASS | `001-content-collection` 브랜치 생성됨 |
| IV. Error Handling | ✅ PASS | 구체적 예외 타입 사용, 사용자 친화적 한글 메시지 |
| V. Phase-Based Development | ✅ PASS | Phase 1 완료 조건: `about.confirmed = true` |
| VI. Simplicity First | ✅ PASS | YAGNI 준수, 파일 기반 저장소 (DB 불필요) |
| VII. Environment & Security | ✅ PASS | API Key는 `.env`에 보관, 하드코딩 금지 |

**Constitution Check 결과**: 모든 원칙 준수, Phase 0 진행 가능

## Project Structure

### Documentation (this feature)

```text
specs/001-content-collection/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output (API contracts)
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)

```text
backend/
├── build.gradle.kts
├── settings.gradle.kts
├── src/
│   ├── main/
│   │   ├── java/com/folio4me/
│   │   │   ├── FolioApplication.java
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── SessionController.java
│   │   │   │   └── ConversationController.java
│   │   │   ├── model/
│   │   │   │   ├── Session.java
│   │   │   │   ├── Portfolio.java
│   │   │   │   ├── PersonalInfo.java
│   │   │   │   ├── WorkExperience.java
│   │   │   │   ├── Education.java
│   │   │   │   ├── Project.java
│   │   │   │   ├── Award.java
│   │   │   │   ├── Activity.java
│   │   │   │   ├── Certification.java
│   │   │   │   ├── TechnicalSkills.java
│   │   │   │   └── About.java
│   │   │   ├── service/
│   │   │   │   ├── SessionService.java
│   │   │   │   ├── ConversationService.java
│   │   │   │   ├── ValidationService.java
│   │   │   │   ├── StorageService.java
│   │   │   │   └── AiService.java
│   │   │   ├── dto/
│   │   │   │   ├── ConversationRequest.java
│   │   │   │   └── ConversationResponse.java
│   │   │   └── exception/
│   │   │       ├── InvalidInputException.java
│   │   │       └── SessionNotFoundException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── messages_ko.properties
│   └── test/
│       └── java/com/folio4me/
│           ├── controller/
│           ├── service/
│           └── integration/
└── data/
    └── {uuid}/
        └── portfolio.json
```

**Structure Decision**: Web application 구조 선택 (backend/ 디렉토리). 현재는 백엔드만 구현하며, 프론트엔드는 추후 `frontend/` 디렉토리에 추가 예정. 파일 기반 저장소를 사용하여 `data/{uuid}/` 구조로 세션별 데이터 관리.

## Complexity Tracking

> 현재 Constitution 위반 없음. 복잡도 정당화 불필요.

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | N/A | N/A |

---

## Phase 0: Research

*다음 단계에서 research.md 생성*

### 조사 필요 항목

1. **Spring Boot 3.x + Java 17 프로젝트 초기 설정**
   - Gradle Kotlin DSL 설정
   - 의존성 관리 (Jackson, Validation, Test)

2. **파일 기반 JSON 저장소 구현 패턴**
   - 동시성 처리 (세션별 파일 잠금)
   - UUID 기반 디렉토리 구조

3. **대화형 AI 통합 방식**
   - OpenAI/Anthropic API 호출 패턴
   - 프롬프트 템플릿 관리

4. **Step 기반 상태 머신 구현**
   - 12단계 대화 흐름 관리
   - Step별 필수/선택 필드 검증

---

## Phase 1: Design

*다음 단계에서 data-model.md, contracts/, quickstart.md 생성*

### 설계 대상

1. **Data Model**: portfolio.json 스키마 기반 Java 엔티티
2. **API Contracts**: 세션/대화 REST 엔드포인트
3. **Quickstart**: 로컬 개발 환경 설정 및 실행 가이드
