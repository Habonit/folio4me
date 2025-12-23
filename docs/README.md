# 문서 가이드

이 디렉토리는 프로젝트의 모든 문서를 관리한다.

---

## 1. 디렉토리 구조

```
docs/
├── README.md                 # 이 문서
├── api_spec.md               # API 명세서
├── error_spec.md             # 에러 명세서
├── version_change_log.md     # 버전 변경 이력
├── core/                     # 핵심 규칙 및 템플릿 (수정 거의 없음)
└── sdd/                      # 기능 제안서 및 리뷰 리포트
```

---

## 2. 문서 설명

| 문서/디렉토리 | 설명 | 수정 빈도 |
|:-----|:-----|:-----|
| `api_spec.md` | 프로젝트 API 엔드포인트 명세 | 기능 추가 시 |
| `error_spec.md` | 프로젝트 에러 코드 및 처리 방법 | 에러 추가 시 |
| `version_change_log.md` | 버전별 변경 이력 | 릴리즈 시 |
| `core/` | 프로젝트 규칙, 컨벤션, 템플릿 문서 | 거의 없음 |
| `sdd/` | 브랜치별 제안서 및 리뷰 리포트 | 매 개발 사이클 |

---

## 3. 상세 설명

### 3.1 api_spec.md

프로젝트에서 구현한 API 엔드포인트를 정의한다.

- Notion 기능 정의서의 기능 ID와 매핑
- 요청/응답 형식, 에러 코드 포함
- 템플릿: `core/api_spec_template.md`

### 3.2 error_spec.md

프로젝트에서 처리하는 에러를 정의한다.

- Notion 에러 명세서의 에러 ID와 매핑
- 에러 상황, 처리 코드 포함
- 템플릿: `core/error_spec_template.md`

### 3.3 version_change_log.md

버전별 변경 사항을 기록한다.

- Notion 버전 계획서와 매핑
- 릴리즈 시 업데이트
- 템플릿: `core/version_change_log_template.md`

### 3.4 core/

프로젝트의 핵심 규칙과 템플릿을 포함한다.

- **수정이 거의 일어나지 않는 문서**를 관리
- 프로젝트 규칙, TDD 방법론, CI/CD 워크플로우 등
- 상세: [core/README.md](./core/README.md)

### 3.5 sdd/

브랜치별 기능 제안서와 리뷰 리포트를 관리한다.

- `{브랜치 식별자}.md`: 기능 제안서
- `{브랜치 식별자}_report.md`: 리뷰 리포트
- 릴리즈 후 `history/`로 이동
- 상세: [sdd/README.md](./sdd/README.md)

---

## 4. 참조

- [Core 문서 가이드](./core/README.md)
- [SDD 문서 가이드](./sdd/README.md)

---
