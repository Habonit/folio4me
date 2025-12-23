# CI/CD 워크플로우

---

## 1. 개발 워크플로우 개요

```
┌─────────────────────────────────────────────────────────────────────┐
│  [1] dev에서 feature/ 브랜치 분기                                  │
│                    │                                                │
│                    ▼                                                │
│  [2] docs/sdd/{브랜치 식별자}.md 작성                               │
│      (improvement_proposal_template.md 참고)                        │
│                    │                                                │
│                    ▼                                                │
│  [3] Claude Code에게 해당 문서 기반 코딩 요청                          │
│                    │                                                │
│                    ▼                                                │
│  [4] 사람 개입 + TDD 개발                                            │
│                    │                                                │
│                    ▼                                                │
│  [5] Push 전 Claude 리뷰 (review_before_push.md)                   │
│      └── ❌ → 수정 후 재리뷰                                         │
│      └── ✅ → docs/sdd/{브랜치 식별자}_report.md 저장               │
│                    │                                                │
│                    ▼                                                │
│  [6] Push + PR (feature → dev)                                      │
│                    │                                                │
│                    ▼                                                │
│  [7] CI: Unit + Lint + Type Check                                   │
│      └── ❌ → 수정 후 push (브랜치) → CI 재실행               │
│      └── ✅ → 다음 단계                                              │
│                    │                                                │
│                    ▼                                                │
│  [8] dev 머지 (모든 feature 브랜치에서)                               │
│                    │                                                │
│                    ▼                                                │
│  [9] 당번 개발자: Claude 리뷰 (review_before_merge.md)               │
│      └── ❌ → 수정 후 재리뷰                                         │
│      └── ✅ → docs/sdd/{YYYYMMDD}_dev_report.md 저장                     │
│                    │                                                │
│                    ▼                                                │
│  [10] 당번 개발자: prep/{YYYYMMDD}_{작업자} 브랜치 생성               │
│       ├── docs/sdd/{브랜치 식별자}_* → docs/sdd/history/ 이동       │
│       └── docs/ 문서 업데이트 확인                                    │
│                    │                                                │
│                    ▼                                                │
│  [11] Push + PR (prep → dev)                                        │
│       └── PR에서 Files changed 확인 (docs만 변경되었는지)             │
│       └── CI 통과 후 머지                                            │
│                    │                                                │
│                    ▼                                                │
│  [12] PR (dev → main)                                               │
│                    │                                                │
│                    ▼                                                │
│  [13] CI: Unit + Lint + Type + Integration + E2E + Build            │
│       └── ❌ → 수정 후 push (dev 브랜치) → CI 재실행                  │
│       └── ✅ → 다음 단계                                             │
│                    │                                                │
│                    ▼                                                │
│  [14] main 머지 → 배포                                               │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2. 단계별 상세

### [1] 브랜치 분기

```bash
git checkout dev
git pull origin dev
git checkout -b feature/{YYYYMMDD}_{JIRA-ID}_{기능정의서ID}_{작업자}
```

### [2] 제안서 작성

`docs/sdd/{브랜치 식별자}.md` 파일 생성

```bash
# 예시
docs/sdd/20250115_PROJ-123_FD-001_honggildong.md
```

`improvement_proposal_template.md`를 참고하여 작성한다.

### [3] Claude Code 코딩 요청

```bash
claude "docs/sdd/20250115_PROJ-123_FD-001_honggildong.md 기반으로 구현해줘"
```

### [4] TDD 개발

- 사람이 개입하여 코드 수정
- 관련 모듈에 대해 TDD 진행 (Red → Green → Refactor)
- `tdd_constitution_*.md` 준수

### [5] Push 전 리뷰

```bash
claude "docs/core/review_before_push.md의 {브랜치 식별자}을 현재 브랜치로 해서 리뷰해줘"
```

| 결과 | 행동 |
|:-----|:-----|
| ❌ | 수정 후 재리뷰 |
| ✅ | `docs/sdd/{브랜치 식별자}_report.md` 저장 |

### [6] Push + PR

```bash
git add .
git commit -m "feat(브랜치 식별자): {기능 설명}"
git push origin feature/{YYYYMMDD}_{JIRA-ID}_{기능정의서ID}_{작업자}
# GitHub에서 PR 생성 (feature → dev)
```

### [7] CI (feature → dev)

| 검증 항목 | 실행 |
|:---------|:-----|
| Unit Test | ✅ |
| Lint | ✅ |
| Type Check | ✅ |

**예상 시간**: 2-3분

| 결과 | 행동 |
|:-----|:-----|
| ❌ | 로컬에서 수정 → 같은 브랜치에 push → CI 재실행 |
| ✅ | PR 머지 진행 |

### [8] dev 머지

모든 feature 브랜치에서 dev로 머지한다.

### [9] 당번 개발자: Main 머지 전 리뷰

```bash
claude "docs/core/review_before_merge.md 참고해서 main 머지 전 리뷰해줘"
```

| 결과 | 행동 |
|:-----|:-----|
| ❌ | 수정 후 재리뷰 |
| ✅ | `docs/sdd/{YYYYMMDD}_dev_report.md` 저장 |

### [10] 당번 개발자: prep 브랜치 생성 + 정리 작업

```bash
git checkout dev
git pull origin dev
git checkout -b prep/{YYYYMMDD}_{작업자}

# 브랜치별 문서 히스토리로 이동
mv docs/sdd/{브랜치 식별자}*.md docs/sdd/history/
```

### [11] Push + PR (prep → dev)

```bash
git add .
git commit -m "docs: 문서 정리 및 업데이트"
git push origin prep/{YYYYMMDD}_{작업자}
# GitHub에서 PR 생성 (prep → dev)
```

**PR 체크리스트**:
- [ ] Files changed 확인 (docs/ 외 변경 없는지)
- [ ] CI 통과
- [ ] 머지

### [12] PR (dev → main)

GitHub에서 PR 생성 (dev → main)

### [13] CI (dev → main)

| 검증 항목 | 실행 |
|:---------|:-----|
| Unit Test | ✅ |
| Lint | ✅ |
| Type Check | ✅ |
| Integration Test | ✅ |
| E2E Test | ✅ |
| Build | ✅ |

**예상 시간**: 10-15분

| 결과 | 행동 |
|:-----|:-----|
| ❌ | 로컬에서 수정 → dev 브랜치에 push → CI 재실행 |
| ✅ | PR 머지 진행 |

### [14] main 머지 → 배포

main 브랜치에 반영되면 프로덕션 배포 진행

---

## 3. Claude Code 리뷰

| 시점 | 템플릿 | 리뷰 기준 |
|:-----|:-------|:---------|
| **Push 전** | `docs/core/review_before_push.md` | constitution + tdd + 기능 구현 |
| **Main 머지 전** | `docs/core/review_before_merge.md` | constitution + tdd + 전체 코드 |

---

## 4. CI 파이프라인

### feature → dev

| 검증 항목 | 실행 | 목적 |
|:---------|:-----|:-----|
| Unit Test | ✅ | 코드 로직 검증 |
| Lint | ✅ | 스타일/컨벤션 준수 |
| Type Check | ✅ | 타입 안정성 확보 |

### dev → main

| 검증 항목 | 실행 | 목적 |
|:---------|:-----|:-----|
| Unit Test | ✅ | 코드 로직 검증 |
| Lint | ✅ | 스타일/컨벤션 준수 |
| Type Check | ✅ | 타입 안정성 확보 |
| Integration Test | ✅ | 모듈 연동 검증 |
| E2E Test | ✅ | 사용자 시나리오 검증 |
| Build | ✅ | 배포 가능 상태 확인 |

---

## 5. 검증 항목 정의

### Unit Test (단위 테스트)

개별 함수/클래스가 의도대로 동작하는지 검증한다.

| 항목 | 내용 |
|:-----|:-----|
| **검증 대상** | 함수, 메서드, 클래스 단위 |
| **외부 의존성** | Mock 처리 |
| **실행 시간** | 수 초 ~ 1분 |
| **실패 의미** | 코드 로직 오류 |

### Lint (코드 스타일 검사)

코드 스타일, 포맷팅, 잠재적 오류를 정적 분석한다.

| 항목 | 내용 |
|:-----|:-----|
| **검증 대상** | 코드 스타일, 포맷팅, 안티패턴 |
| **실행 시간** | 수 초 |
| **실패 의미** | 컨벤션 위반, 잠재적 버그 |

### Type Check (타입 검사)

정적 타입 분석으로 타입 불일치 오류를 사전에 검출한다.

| 항목 | 내용 |
|:-----|:-----|
| **검증 대상** | 타입 힌트, 인터페이스 일치 |
| **실행 시간** | 수 초 ~ 1분 |
| **실패 의미** | 타입 불일치, 런타임 에러 가능성 |

### Integration Test (통합 테스트)

여러 모듈/서비스 간 연동이 정상 동작하는지 검증한다.

| 항목 | 내용 |
|:-----|:-----|
| **검증 대상** | 모듈 간 연동, API 호출, DB 연결 |
| **외부 의존성** | 실제 사용 또는 테스트 컨테이너 |
| **실행 시간** | 1-5분 |
| **실패 의미** | 모듈 간 인터페이스 불일치 |

### E2E Test (End-to-End 테스트)

실제 사용자 시나리오를 브라우저/앱에서 검증한다.

| 항목 | 내용 |
|:-----|:-----|
| **검증 대상** | 사용자 시나리오 전체 흐름 |
| **외부 의존성** | 실제 환경 (또는 스테이징) |
| **실행 시간** | 5-15분 |
| **실패 의미** | 사용자 경험 장애 |

### Build (빌드)

배포 가능한 산출물을 생성할 수 있는지 확인한다.

| 항목 | 내용 |
|:-----|:-----|
| **검증 대상** | 컴파일, 번들링, 패키징 |
| **실행 시간** | 1-5분 |
| **실패 의미** | 배포 불가 |

---

## 6. CI 실패 시 대응

| 실패 항목 | 원인 | 대응 |
|:---------|:-----|:-----|
| Unit Test | 코드 로직 오류 | 테스트 또는 코드 수정 |
| Lint | 스타일 위반 | `ruff format .` 등 자동 수정 |
| Type Check | 타입 불일치 | 타입 힌트 수정 |
| Integration | 모듈 간 불일치 | 인터페이스 확인, Mock 점검 |
| E2E | 시나리오 실패 | 전체 흐름 디버깅 |
| Build | 빌드 실패 | 의존성, 설정 확인 |

---

## 7. 참조

- [프로젝트 규칙](./constitution.md)
- 테스트 원칙 (TDD)
  - [Python](./tdd_constitution_python.md)
  - [Java](./tdd_constitution_java.md)
  - [TypeScript](./tdd_constitution_typescript.md)
- [제안서 템플릿](./improvement_proposal_template.md)
- [리뷰 템플릿 - Push 전](./review_before_push.md)
- [리뷰 템플릿 - Main 머지 전](./review_before_merge.md)
- 워크플로우 템플릿
  - [Python](./workflow_template_python.yaml)
  - [Java](./workflow_template_java.yaml)
  - [TypeScript](./workflow_template_typescript.yaml)

---