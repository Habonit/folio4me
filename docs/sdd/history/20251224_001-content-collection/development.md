# Development Phases

> folio4me 개발 단계 정의서

---

## 개요

folio4me 프로젝트는 3개의 개발 단계로 구성됩니다. 각 단계는 순차적으로 진행되며, 이전 단계의 Output이 다음 단계의 Input이 됩니다.

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Phase 1       │     │   Phase 2       │     │   Phase 3       │
│   Content       │ ──▶ │   Style         │ ──▶ │   Generation    │
│   Collection    │     │   Selection     │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
     portfolio.json          style.json           포트폴리오 결과물
```

---

## Phase 1: Content Collection (콘텐츠 수집)

> 사용자와 대화를 통해 포트폴리오에 필요한 콘텐츠를 수집하는 단계

### Prerequisite

| 항목 | 설명 |
|:-----|:-----|
| 세션 시작 | 새로운 사용자 세션 생성 |
| UUID 발급 | UUID v4 형식으로 세션 식별자 발급 |
| 디렉토리 생성 | `data/{uuid}/` 디렉토리 생성 |

### Input

| 항목 | 타입 | 설명 |
|:-----|:-----|:-----|
| 사용자 대화 입력 | text | Step 0~11에 걸친 대화 응답 |
| 증명사진 | image (optional) | 사용자 업로드 이미지 |

### Output

| 항목 | 경로 | 설명 |
|:-----|:-----|:-----|
| portfolio.json | `data/{uuid}/portfolio.json` | 수집된 포트폴리오 콘텐츠 |

### 완료 조건

```
portfolio.json의 about.confirmed = true
```

### 참조 문서

- `conversation-flow-spec.md` - 대화 흐름 명세
- `portfolio-data-schema.md` - 데이터 스키마

---

## Phase 2: Style Selection (스타일 선택)

> 사용자의 스타일 선호도를 파악하고 적합한 템플릿을 선택하는 단계

### Prerequisite

| 항목 | 설명 |
|:-----|:-----|
| Phase 1 완료 | `portfolio.json`의 `about.confirmed: true` |
| 템플릿 준비 | `templates/` 디렉토리에 템플릿 존재 |

### Input

| 항목 | 타입 | 설명 |
|:-----|:-----|:-----|
| 사용자 스타일 입력 | text | 스타일 선호도 평문 설명 |
| 템플릿 메타데이터 | json | `templates/{id}/metadata.json` |
| 템플릿 미리보기 | image/gif | `templates/{id}/preview.png`, `preview.gif` |

### Output

| 항목 | 경로 | 설명 |
|:-----|:-----|:-----|
| style.json | `data/{uuid}/style.json` | 스타일 선호도 및 선택된 템플릿 정보 |

### 완료 조건

```
style.json의 selectedTemplate.confirmed = true
style.json의 meta.status = "complete"
```

### 참조 문서

- `style-and-generation-talk-flow.md` - 스타일 질의 대화 흐름 (Step 1~2)
- `style_data_schema.md` - 스타일 데이터 스키마
- `template_schema.md` - 템플릿 구조 및 메타데이터 스키마

---

## Phase 3: Generation (포트폴리오 생성)

> 선택된 템플릿과 데이터를 사용자 디렉토리에 복사하고, 추가 작업 안내를 제공하는 단계

### Prerequisite

| 항목 | 설명 |
|:-----|:-----|
| Phase 1 완료 | `portfolio.json` 생성 완료 |
| Phase 2 완료 | `style.json`의 `meta.status: "complete"` |
| 템플릿 소스 | `templates/{templateId}/src/` 존재 |

### Input

| 항목 | 경로 | 설명 |
|:-----|:-----|:-----|
| portfolio.json | `data/{uuid}/portfolio.json` | 포트폴리오 콘텐츠 |
| style.json | `data/{uuid}/style.json` | 선택된 템플릿 정보 |
| 템플릿 소스 | `templates/{templateId}/src/` | 템플릿 소스 코드 |

### Output

| 항목 | 경로 | 설명 |
|:-----|:-----|:-----|
| 복사된 템플릿 | `data/{uuid}/src/` | 템플릿 소스 복사본 |
| 복사된 데이터 | `data/{uuid}/src/public/portfolio.json` | 포트폴리오 데이터 복사본 |
| 복사된 스타일 | `data/{uuid}/src/public/style.json` | 스타일 데이터 복사본 |
| 안내 문서 | `data/{uuid}/src/README.md` | 추가 작업 안내 (환경 구성, 실행 방법, 추가 이미지) |

### 처리 방식

```
┌─────────────────────────────────────────────────────────────────┐
│  LLM 사용: X (파일 복사만)                                       │
├─────────────────────────────────────────────────────────────────┤
│  1. style.json에서 templateId 추출                               │
│  2. templates/{templateId}/src/ → data/{uuid}/src/ 복사          │
│  3. data/{uuid}/*.json → data/{uuid}/src/public/ 복사 (통일)     │
│     • portfolio.json → src/public/portfolio.json                │
│     • style.json → src/public/style.json                        │
│  4. 사용자 화면에 src/README.md 내용 표시                         │
└─────────────────────────────────────────────────────────────────┘
```

### 최종 디렉토리 구조

**모든 템플릿 (통일):**
```
data/{uuid}/
├── portfolio.json    ← Phase 1에서 생성 (원본)
├── style.json        ← Phase 2에서 생성 (원본)
└── src/              ← Phase 3에서 생성
    ├── README.md       ← 추가 작업 안내 (사용자에게 표시)
    ├── public/
    │   ├── portfolio.json  ← 복사됨 (템플릿이 참조)
    │   └── style.json      ← 복사됨
    └── ...             ← 템플릿 파일들
```

**데이터 접근 경로 (통일 규칙):**
- Next.js 템플릿: `/portfolio.json` (public 폴더 규칙)
- 정적 HTML 템플릿: `./public/portfolio.json`

### 완료 조건

```
templates/{templateId}/src/ → data/{uuid}/src/ 복사 완료
data/{uuid}/*.json → data/{uuid}/src/public/ 복사 완료
사용자에게 README.md 내용 표시
```

### 참조 문서

- `style-and-generation-talk-flow.md` - 생성 대화 흐름 (Step 3~4)
- `template_schema.md` - 템플릿 구조 및 메타데이터 스키마

---

## 단계별 데이터 흐름

```
Phase 1                    Phase 2                    Phase 3
────────────────────────────────────────────────────────────────────

[사용자 입력]              [사용자 입력]
     │                          │
     ▼                          ▼
┌──────────┐              ┌──────────┐              ┌──────────┐
│ 대화 처리 │              │ 스타일   │              │ 템플릿   │
│ (LLM)    │              │ 매칭     │              │ + 데이터 │
│          │              │ (LLM)    │              │ 복사     │
└──────────┘              └──────────┘              └──────────┘
     │                          │                        │
     ▼                          ▼                        ▼
┌──────────┐              ┌──────────┐              ┌──────────┐
│portfolio │─────────────▶│ style    │─────────────▶│ 포트폴리오│
│.json     │              │ .json    │              │ 결과물   │
└──────────┘              └──────────┘              └──────────┘
```

---

## 디렉토리 구조

```
/folio4me
  ├── data/
  │   └── {uuid}/
  │       ├── portfolio.json    ← Phase 1 Output (원본)
  │       ├── style.json        ← Phase 2 Output (원본)
  │       └── src/              ← Phase 3 Output
  │           ├── README.md       ← 추가 작업 안내
  │           ├── public/
  │           │   ├── portfolio.json  ← 복사됨
  │           │   └── style.json      ← 복사됨
  │           └── ...             ← 템플릿 파일들
  │
  ├── templates/
  │   ├── 1/
  │   │   ├── metadata.json
  │   │   ├── preview.png
  │   │   ├── preview.gif
  │   │   └── src/
  │   │       ├── README.md       ← 추가 작업 안내 (필수)
  │   │       ├── public/         ← 정적 자산 및 데이터 저장소
  │   │       └── ...             ← public/portfolio.json 참조하는 템플릿
  │   ├── 2/
  │   └── 3/
  │
  └── project_docs/
      ├── conversation-flow-spec.md
      ├── portfolio-data-schema.md
      ├── style-and-generation-talk-flow.md
      ├── style_data_schema.md
      ├── template_schema.md
      ├── template-modification-guide.md
      └── development.md          ← 본 문서
```

---

## 변경 이력

| 날짜 | 버전 | 변경 내용 |
|:-----|:-----|:----------|
| 2025-12-19 | 1.0.0 | 최초 작성 |
| 2025-12-19 | 1.1.0 | Phase 3 업데이트: 템플릿 복사 + README.md 표시 방식으로 변경 |
| 2025-12-19 | 1.2.0 | Phase 3 업데이트: json 파일을 src/ 내부로 복사하는 방식으로 변경 |
| 2025-12-19 | 1.3.0 | Phase 3 업데이트: json 파일을 `src/public/` 폴더로 통일하여 복사 (템플릿 타입별 분기 제거) |
