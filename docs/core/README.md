# Core 문서 가이드

이 디렉토리는 프로젝트의 핵심 규칙, 컨벤션, 템플릿 문서를 포함한다.

---

## 1. 문서 목록

| 문서 | 설명 |
|:-----|:-----|
| `constitution.md` | 프로젝트 개발 규칙 및 코드 컨벤션 |
| `style.md` | 마크다운 문서 작성 스타일 가이드 |
| `ci_cd.md` | 개발 워크플로우 및 CI/CD 파이프라인 |
| `tdd_constitution_python.md` | Python TDD 방법론 |
| `tdd_constitution_java.md` | Java TDD 방법론 |
| `tdd_constitution_typescript.md` | TypeScript/React TDD 방법론 |
| `improvement_proposal_template.md` | 기능 개선 제안서 템플릿 |
| `api_spec_template.md` | API 명세서 템플릿 |
| `error_spec_template.md` | 에러 명세서 템플릿 |
| `project_readme_template.md` | 프로젝트 README 템플릿 |
| `pull_request_template.md` | Pull Request 템플릿 |
| `version_change_log_template.md` | 버전 변경 이력 템플릿 |
| `review_before_push.md` | Push 전 Claude 리뷰 템플릿 |
| `review_before_merge.md` | Main 머지 전 Claude 리뷰 템플릿 |

---

## 2. 문서 참조 관계

### 2.1 내부 참조 관계

```
                        ┌─────────────────────────────────┐
                        │           ci_cd.md              │
                        │        (개발 워크플로우)          │
                        └────────────────┬────────────────┘
                                         │
         ┌───────────┬───────────┬───────┼───────┬───────────┬───────────┐
         │           │           │       │       │           │           │
         ▼           ▼           ▼       ▼       ▼           ▼           ▼
┌─────────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐
│constitution │ │tdd_*    │ │improve- │ │review_  │ │review_  │ │workflow_│
│.md          │ │.md (3개) │ │ment_    │ │before_  │ │before_  │ │*.yaml   │
│             │ │         │ │proposal_│ │push.md  │ │merge.md │ │(3개)    │
│(프로젝트규칙)│ │(TDD가이드)│ │template │ │         │ │         │ │         │
│             │ │         │ │.md      │ │(Push전) │ │(머지전) │ │(CI템플릿)│
└─────────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘
```

### 2.2 ci_cd.md 참조 문서

| 참조 문서 | 용도 |
|:-----|:-----|
| `constitution.md` | 개발 규칙 준수 기준 |
| `tdd_constitution_python.md` | Python TDD 가이드 |
| `tdd_constitution_java.md` | Java TDD 가이드 |
| `tdd_constitution_typescript.md` | TypeScript TDD 가이드 |
| `improvement_proposal_template.md` | 제안서 작성 참고 |
| `review_before_push.md` | Push 전 리뷰 템플릿 |
| `review_before_merge.md` | Main 머지 전 리뷰 템플릿 |
| `workflow_template_*.yaml` | CI 워크플로우 템플릿 |

---

## 3. 외부 참조 관계 (Notion)

일부 템플릿 문서는 Notion에 관리되는 외부 문서를 참조한다.

```
┌─────────────────────────────────────────────────────────────────────────┐
│                            Notion (외부)                                 │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│   ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐        │
│   │   에러 명세서    │  │   기능 정의서    │  │   버전 계획서    │        │
│   │                 │  │                 │  │                 │        │
│   │ - 에러 ID 관리   │  │ - 기능 ID 관리   │  │ - 버전별 계획    │        │
│   │ - 에러 상황 정의 │  │ - 요구사항 정의  │  │ - 릴리즈 일정    │        │
│   └────────┬────────┘  └────────┬────────┘  └────────┬────────┘        │
│            │                    │                    │                 │
└────────────┼────────────────────┼────────────────────┼─────────────────┘
             │                    │                    │
             ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         docs/core/ (내부)                                 │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                         │
│   ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐        │
│   │ error_spec_     │  │ api_spec_       │  │ version_change_ │        │
│   │ template.md     │  │ template.md     │  │ log_template.md │        │
│   │                 │  │                 │  │                 │        │
│   │ 에러 명세서 참조  │  │ 기능 정의서 참조  │  │ 버전 계획서 참조  │        │
│   └─────────────────┘  └─────────────────┘  └─────────────────┘        │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.1 외부 참조 상세

| 내부 문서 | 외부 참조 (Notion) | 참조 내용 |
|:-----|:-----|:-----|
| `error_spec_template.md` | 에러 명세서 | 에러 ID, 에러 상황, 처리 방법 |
| `api_spec_template.md` | 기능 정의서 | 기능 ID, API와 기능 매핑 |
| `version_change_log_template.md` | 버전 계획서 | 버전별 개발 계획, 릴리즈 일정 |

---

## 4. 문서 사용 시점

| 시점 | 사용 문서 |
|:-----|:-----|
| 프로젝트 시작 | `constitution.md`, `style.md` |
| 기능 개발 시작 | `improvement_proposal_template.md` |
| TDD 개발 | `tdd_constitution_*.md` |
| API 설계 | `api_spec_template.md` |
| 에러 정의 | `error_spec_template.md` |
| Push 전 | `review_before_push.md` |
| PR 생성 | `pull_request_template.md` |
| Main 머지 전 | `review_before_merge.md` |
| 릴리즈 | `version_change_log_template.md` |

---

## 5. 참조

- [프로젝트 규칙](./constitution.md)
- [문서 스타일 가이드](./style.md)
- [개발 워크플로우](./ci_cd.md)

---
