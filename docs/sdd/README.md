# SDD (Spec-Driven Development) 문서

이 디렉토리는 스펙주도개발에 사용하는 브랜치별 제안서와 리뷰 리포트를 관리한다.

---

## 1. 디렉토리 구조

```
sdd/
├── README.md                              # 이 문서
├── {브랜치 식별자}.md                       # 기능 제안서
├── {브랜치 식별자}_report.md               # 리뷰 리포트
└── history/                               # 머지 완료된 문서 보관
    └── {브랜치 식별자}*.md
```

---

## 2. 문서 유형

| 문서 | 설명 | 생성 시점 |
|:-----|:-----|:-----|
| `{브랜치 식별자}.md` | 기능 제안서 | 브랜치 생성 후 |
| `{브랜치 식별자}_report.md` | Claude 리뷰 리포트 | Push 전 리뷰 후 |

---

## 3. 문서 작성 규칙

### 3.1 파일명

브랜치 식별자를 파일명으로 사용한다.

```
# 브랜치: feature/20250115_PROJ-123_FD-001_honggildong
# 브랜치 식별자: 20250115_PROJ-123_FD-001_honggildong

20250115_PROJ-123_FD-001_honggildong.md          # 제안서
20250115_PROJ-123_FD-001_honggildong_report.md   # 리뷰 리포트
```

### 3.2 작성 참조

| 항목 | 참조 문서 |
|:-----|:-----|
| 문서 스타일 | `core/style.md` |
| 제안서 템플릿 | `core/improvement_proposal_template.md` |

---

## 4. 문서 라이프사이클

```
[1] 브랜치 생성
         │
         ▼
[2] 제안서 작성 ──────────────────────────────────────┐
    sdd/{브랜치 식별자}.md                             │
         │                                           │
         ▼                                           │
[3] 개발 진행 (TDD)                                   │
         │                                           │
         ▼                                           │
[4] Push 전 리뷰 → 리포트 생성                         │
    sdd/{브랜치 식별자}_report.md                      │
         │                                           │
         ▼                                           │
[5] PR → dev 머지                                    │
         │                                           │
         ▼                                           │
[6] Main 머지 준비 (prep 브랜치)                       │
    └── sdd/{브랜치 식별자}*.md → sdd/history/ 이동 ◄──┘
```

---

## 5. history 디렉토리

머지가 완료된 문서는 `history/`로 이동하여 보관한다.

- Main 머지 전 prep 브랜치에서 이동
- 과거 제안서 및 리뷰 이력 조회 용도

---

## 6. 참조

- [문서 스타일 가이드](../core/style.md)
- [제안서 템플릿](../core/improvement_proposal_template.md)
- [개발 워크플로우](../core/ci_cd.md)

---
