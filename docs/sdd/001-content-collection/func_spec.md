# Notion 기능 정의서 데이터베이스 템플릿

---

## 1. 메인 데이터베이스: 기능 정의서

### 속성(Property) 구조

| 속성명 | 타입 | 설명 | 옵션/설정 |
|:-------|:-----|:-----|:---------|
| **ID** | Text | 분류코드-번호 | 예: F-001 |
| **기능명** | Text| 기능 이름 | - |
| **분류** | Select | 기능 분류 | F, S, API, AI, INT |
| **개발 버전** | Select | 배포 목표 버전 | - |
| **우선순위** | Select | 중요도 | P0, P1, P2, P3 |
| **상태** | Status | 진행 상태 | Backlog, To Do, In Progress, Review, Done |
| **설명** | Text | 간단한 기능 설명 | - |
| **연관 요구사항** | Relation | 연관된 요구사항 | → 요구사항 정의서 DB |
| **연관 화면** | Relation | 연관된 화면 | → 화면 정의서 DB |
| **연관 템플릿 버전** | Relation | 기능정의서 템플릿 버전 | → 탬플릿 히스토리 DB |
| **생성일** | Created time | 자동 생성 | - |
| **수정일** | Last edited time | 자동 수정 | - |

---

## 2. Select 옵션 상세

### 분류 (Select)

| 옵션 | 색상 | 설명 |
|:-----|:-----|:-----|
| `F - Feature` | 🔵 Blue | 사용자가 수행하는 동작/기능 |
| `S - Screen` | 🟢 Green | UI 화면 단위 |
| `API` | 🟣 Purple | 우리가 만드는 내부 API |
| `AI` | 🟠 Orange | AI 모델 관련 처리 |
| `INT - Integration` | 🔴 Red | 외부 서비스 연동 |

### 버전 (Select)

| 옵션 | 색상 | 설명 |
|:-----|:-----|:-----|
| `v0.1.0` | default 사용 | 최초 릴리즈 |


### 우선순위 (Select)

| 옵션 | 색상 | 설명 |
|:-----|:-----|:-----|
| `P0 - Critical` | 🔴 Red | 없으면 서비스 불가 |
| `P1 - High` | 🟠 Orange | 주요 기능, 첫 릴리즈 포함 |
| `P2 - Medium` | 🟡 Yellow | 있으면 좋음, 다음 릴리즈 가능 |
| `P3 - Low` | ⚪ Gray | 나중에 해도 됨 |

### 상태 (Status)

| 옵션 | 그룹 | 설명 |
|:-----|:-----|:-----|
| `Backlog` | To-do | 목록에 있음, 착수 전 |
| `To Do` | To-do | 이번 스프린트에 진행 예정 |
| `In Progress` | In progress | 현재 작업 중 |
| `Review` | In progress | 코드 리뷰 또는 QA 중 |
| `Done` | Complete | 작업 완료 |