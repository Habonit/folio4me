<!--
Sync Impact Report
==================
Version change: N/A → 1.0.0
Modified principles: N/A (initial creation)
Added sections:
  - Core Principles (7 principles)
  - Development Workflow
  - CI/CD & Quality Gates
  - Governance
Removed sections: N/A
Templates requiring updates:
  - .specify/templates/plan-template.md: ✅ compatible (uses Constitution Check section)
  - .specify/templates/spec-template.md: ✅ compatible (no constitution references)
  - .specify/templates/tasks-template.md: ✅ compatible (no constitution references)
Follow-up TODOs: None
-->

# folio4me Constitution

folio4me 프로젝트의 개발 규칙과 핵심 원칙을 정의합니다.

## Core Principles

### I. TDD-First Development (NON-NEGOTIABLE)

모든 신규 기능과 버그 수정에 Test-Driven Development를 적용합니다.

**규칙:**
- 테스트를 먼저 작성하고, 실패하는 것을 확인한 후, 통과하는 최소한의 코드를 구현합니다 (Red-Green-Refactor)
- 테스트 코드가 먼저 실패해야만 구현을 시작할 수 있습니다
- 테스트 피라미드 비율 준수: Unit 70% : Integration 25% : E2E 5%
- 모든 테스트는 독립적으로 실행 가능해야 하며, 다른 테스트에 영향을 주거나 받지 않아야 합니다

**적용:**
- Python: pytest + AAA 패턴 (Arrange-Act-Assert)
- Java: JUnit 5 + Mockito + Given-When-Then 패턴
- TypeScript: Jest + React Testing Library + AAA 패턴

**근거:** 테스트가 개발을 이끌어 "동작하는 깔끔한 코드(Clean code that works)"를 달성합니다. 리팩토링 안전망을 제공하고 버그 재발을 방지합니다.

### II. Code Style & Convention Compliance

일관된 코드 스타일과 컨벤션을 준수합니다.

**규칙:**
- 모든 함수/메서드에 타입 힌트 필수 (Python 3.10+, TypeScript 5.0+, Java 17+)
- 모든 public 함수/메서드에 Docstring/Javadoc/JSDoc 필수
- 문서 및 주석은 한글로 작성 (기술 용어는 영어 허용)
- Import 순서: 표준 라이브러리 → 서드파티 → 로컬 모듈
- 네이밍 컨벤션:
  - Python: snake_case (함수/변수), PascalCase (클래스), UPPER_SNAKE_CASE (상수)
  - Java: camelCase (메서드/변수), PascalCase (클래스), UPPER_SNAKE_CASE (상수)
  - TypeScript: camelCase (함수/변수), PascalCase (컴포넌트/타입)

**근거:** 코드 가독성과 유지보수성을 높이고, 팀 협업을 원활하게 합니다.

### III. Git Branch & Commit Convention

체계적인 브랜치 전략과 커밋 규칙을 따릅니다.

**브랜치 규칙:**
- 브랜치 전략: `main ← dev ← feature/~~`
- Feature 브랜치: `feature/{YYYYMMDD}_{JIRA-ID}_{기능정의서ID}_{작업자}`
- 배포 준비 브랜치: `prep/{YYYYMMDD}_{작업자}`
- 항상 `dev` 브랜치에서 분기합니다

**커밋 규칙:**
- 형식: `<type>(<scope>): <subject>`
- Type: feat, fix, docs, style, refactor, test, chore
- 커밋 메시지 본문에 변경 사항을 상세히 기술합니다

**근거:** 변경 이력 추적과 코드 리뷰를 용이하게 합니다.

### IV. Error Handling & Graceful Degradation

체계적인 에러 처리와 우아한 성능 저하를 구현합니다.

**규칙:**
- bare except (Python) / 포괄적 Exception catch (Java) / unknown 타입 미검증 catch (TypeScript) 금지
- 구체적인 예외 타입을 명시하고 적절히 처리합니다
- 사용자에게 친절한 한글 에러 메시지를 제공합니다
- 내부 에러 로깅과 사용자 메시지를 분리합니다
- 부분 기능 실패 시 전체 시스템이 중단되지 않도록 Graceful Degradation을 적용합니다

**근거:** 시스템 안정성을 높이고 사용자 경험을 보호합니다.

### V. Phase-Based Development

3단계 개발 파이프라인을 따릅니다.

**단계:**
1. **Phase 1 (Content Collection)**: 사용자 대화를 통한 콘텐츠 수집 → `portfolio.json` 생성
2. **Phase 2 (Style Selection)**: 스타일 선호도 파악 및 템플릿 선택 → `style.json` 생성
3. **Phase 3 (Generation)**: 선택된 템플릿과 데이터를 조합하여 결과물 생성

**완료 조건:**
- Phase 1: `portfolio.json`의 `about.confirmed = true`
- Phase 2: `style.json`의 `selectedTemplate.confirmed = true` 및 `meta.status = "complete"`
- Phase 3: `data/{uuid}/src/` 디렉토리 생성 완료 및 README.md 표시

**근거:** 명확한 단계별 목표와 검증 기준으로 품질을 보장합니다.

### VI. Simplicity First

단순성을 우선시합니다.

**규칙:**
- 복잡한 로직보다 이해하기 쉬운 코드를 작성합니다
- 과한 최적화를 지양합니다: 교육적 가치 > 성능 최적화
- YAGNI (You Aren't Gonna Need It) 원칙을 준수합니다
- 코드 구조는 일관되고 예측 가능해야 합니다

**근거:** 유지보수 비용을 줄이고 협업을 용이하게 합니다.

### VII. Environment & Security

환경 설정과 보안을 철저히 관리합니다.

**규칙:**
- API Key 등 민감 정보는 `.env` 파일에 보관하고 절대 소스 코드에 하드코딩하지 않습니다
- `.env` 파일은 `.gitignore`에 포함하여 버전 관리에서 제외합니다
- 패키지 관리: Python은 UV, Java는 Gradle, TypeScript는 npm 또는 yarn 사용

**근거:** 보안 사고를 예방하고 환경 일관성을 유지합니다.

## Development Workflow

### 개발 프로세스

1. **브랜치 분기**: `dev`에서 feature 브랜치 분기
2. **제안서 작성**: `docs/sdd/{브랜치 식별자}.md` 작성
3. **TDD 개발**: Red → Green → Refactor 사이클 반복
4. **Push 전 리뷰**: `docs/core/review_before_push.md` 기반 리뷰
5. **PR 생성**: feature → dev PR 생성
6. **CI 통과**: Unit + Lint + Type Check 통과 필수
7. **dev 머지**: 모든 feature 브랜치 머지
8. **Main 머지 전 리뷰**: `docs/core/review_before_merge.md` 기반 리뷰
9. **prep 브랜치**: 문서 정리 및 히스토리 이동
10. **main 머지**: 최종 배포

### 코드 리뷰 체크리스트

- [ ] 타입 힌트 적용 여부
- [ ] Docstring 작성 여부
- [ ] 테스트 코드 존재 여부
- [ ] 한글 주석/문서 규칙 준수
- [ ] 구체적 예외 타입 명시 (bare except 금지)

## CI/CD & Quality Gates

### feature → dev

| 검증 항목 | 목적 |
|:---------|:-----|
| Unit Test | 코드 로직 검증 |
| Lint | 스타일/컨벤션 준수 |
| Type Check | 타입 안정성 확보 |

### dev → main

| 검증 항목 | 목적 |
|:---------|:-----|
| Unit Test | 코드 로직 검증 |
| Lint | 스타일/컨벤션 준수 |
| Type Check | 타입 안정성 확보 |
| Integration Test | 모듈 연동 검증 |
| E2E Test | 사용자 시나리오 검증 |
| Build | 배포 가능 상태 확인 |

### 테스트 실행 도구

| 언어 | 테스트 프레임워크 | 커버리지 도구 |
|:-----|:-----------------|:-------------|
| Python | pytest | pytest-cov |
| Java | JUnit 5 + Mockito | JaCoCo |
| TypeScript | Jest + React Testing Library | Jest --coverage |

## Governance

### 헌법 우선

이 헌법은 모든 다른 개발 관행보다 우선합니다. 헌법과 충돌하는 관행은 헌법을 따릅니다.

### 개정 절차

1. 개정 제안서 작성 (변경 사유, 영향 범위 명시)
2. 팀 리뷰 및 승인
3. 관련 문서 및 템플릿 동기화
4. 버전 업데이트 및 변경 이력 기록

### 버전 정책

- **MAJOR**: 호환성을 깨는 원칙 제거 또는 재정의
- **MINOR**: 새로운 원칙/섹션 추가 또는 기존 지침 확장
- **PATCH**: 명확화, 오타 수정, 비기능적 개선

### 준수 검토

모든 PR과 코드 리뷰에서 헌법 준수 여부를 확인합니다. 복잡성이 추가될 경우 반드시 정당화되어야 합니다.

---

**Version**: 1.0.0 | **Ratified**: 2025-12-23 | **Last Amended**: 2025-12-23
